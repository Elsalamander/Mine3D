package it.elsalamander.mine3d.Game.Game

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import it.elsalamander.mine3d.Game.Event.Set.PausedGameEvent
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Graphic.Engine.MyGLSurfaceView
import it.elsalamander.mine3d.Game.Graphic.OtherView.CountUpTimer
import it.elsalamander.mine3d.R

/****************************************************************
 * Fragment dove viene eseguito il rendering del gioco
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GameFragment : Fragment() {

    lateinit var bomb : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = this.activity as Game

        //crea una intercetta dell'evento di quando si preme il tasto "Back" per navigare la
        //schermata nel fragment PAUSA
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //intercetto il pulsante Back
                    view.findNavController().navigate(R.id.action_game_to_pause)
                }
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val activity = this.activity as Game

        //se non ho già l'istanza di gioco creala
        if(activity.gameInstance == null){
            activity.gameInstance = GameInstance(activity)
        }
        activity.gameFragment = this

        val settings = activity.settings

        val view = inflater.inflate(R.layout.fragment_game, container, false)

        val timer : TextView = view.findViewById(R.id.fragment_game_timer)
            bomb             = view.findViewById(R.id.fragment_game_bomb)
        val pause : ImageButton = view.findViewById(R.id.fragment_game_pause_button)
        val chrono: Chronometer = view.findViewById(R.id.fragment_game_timer_chrono)
        val mySurface : MyGLSurfaceView = view.findViewById(R.id.glSurfaceViewID)

        mySurface.setMyRenderer()

        timer.visibility = if(settings.baseSett.showTimer.getVal()){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }

        bomb.visibility = if(settings.baseSett.showBomb.getVal()){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }
        bomb.text = activity.gameSett?.numberOfBomb().toString()

        pause.setOnClickListener{
            view.findNavController().navigate(R.id.action_game_to_pause)
        }

        chrono.onChronometerTickListener = CountUpTimer(timer)
        chrono.base = Long.MAX_VALUE
        chrono.start()

        return view
    }

    override fun onPause() {
        super.onPause()
        val activity = this.activity as Game

        //chiama la funzione pausa della istanza di gioco
        activity.gameInstance?.let {activity.eventManager.callEvent( PausedGameEvent(it))}
    }

}