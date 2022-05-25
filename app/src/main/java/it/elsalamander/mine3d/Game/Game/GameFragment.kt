package it.elsalamander.mine3d.Game.Game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import it.elsalamander.mine3d.Game.Event.Set.PausedGameEvent
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Graphic.Engine.MyGLSurfaceView
import it.elsalamander.mine3d.Game.Graphic.OtherView.CountUpTimer
import it.elsalamander.mine3d.Game.Settings.BaseSettings.Theme.ThemeList
import it.elsalamander.mine3d.R

/****************************************************************
 * Fragment dove viene eseguito il game, in particolare
 * il rendering del gioco.
 *
 * Il layout è stato realizzato in modo dichiarativo: "fragment_game"
 * Il rendering è stato effettuato tramite un oggetto "GLSurfaceView"
 * che può essere usato come "oggetto grafico" nel file .xml
 *
 * Durante la creazione viene associato l'oggetto che effettua il
 * rendering del game all'oggetto "GLSurfaceView".
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GameFragment : Fragment() {

    lateinit var bomb : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        //View del fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        //oggetti grafici nel game
        val timer     : TextView        = view.findViewById(R.id.fragment_game_timer)
            bomb                        = view.findViewById(R.id.fragment_game_bomb)
        val pause     : ImageButton     = view.findViewById(R.id.fragment_game_pause_button)
        val mySurface : MyGLSurfaceView = view.findViewById(R.id.glSurfaceViewID)

        //Setta l'oggetto renderer
        mySurface.setMyRenderer()

        //impostazioni base
        val settings = activity.settings

        //Rendi visibili o no le textView come impostato nelle impostazioni
        timer.visibility = if(settings.baseSett.showTimer.getVal()){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }

        //Rendi visibili o no le textView come impostato nelle impostazioni
        bomb.visibility = if(settings.baseSett.showBomb.getVal()){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }

        //Aggiusta il colore delle textView e immageButton
        if(settings.baseSett.theme.getVal() == ThemeList.DARK){
            val color = resources.getColor(R.color.black, activity.theme)
            bomb.setTextColor(color)
            timer.setTextColor(color)
            pause.setColorFilter(color)
        }else{
            val color = resources.getColor(R.color.black, activity.theme)
            bomb.setTextColor(color)
            timer.setTextColor(color)
            pause.setColorFilter(color)
        }
        view.findViewById<TextView>(R.id.centre_pointer).setTextColor(resources.getColor(R.color.black, activity.theme))

        //setta il valore della textView anche se viene nascosta.
        bomb.text = activity.gameSett?.numberOfBomb().toString()

        //evento click button pause
        pause.setOnClickListener{
            view.findNavController().navigate(R.id.action_game_to_pause)
        }

        //Setta il timer anche se può venire nascosto
        val myViewModel: CountUpTimer = ViewModelProviders.of(this).get(CountUpTimer::class.java)
        val liveDataObserver: Observer<Int> = Observer<Int> {
                integer -> timer.text = String.format("%02d:%02d", integer/60, integer%60)
        }
        myViewModel.getLiveData().observe(viewLifecycleOwner, liveDataObserver)

        //ritorna la View
        return view
    }

    override fun onPause() {
        super.onPause()
        val activity = this.activity as Game

        //chiama la funzione pausa della istanza di gioco
        activity.gameInstance?.let {
            //il game è stato concluso?
            if(it.grid.toFind == it.grid.scovered){
                //si è stato concluso non salvare
            }else{
                //non è concluso salva lo stato
                activity.eventManager.callEvent(PausedGameEvent(it))
            }
        }
    }

}