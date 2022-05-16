package it.elsalamander.mine3d.Game.Game

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import it.elsalamander.mine3d.Game.Event.Set.QuitGameEvent
import it.elsalamander.mine3d.MainActivity
import it.elsalamander.mine3d.R

/****************************************************************
 * Fragment dove è descritta la schermata di pausa
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GamePauseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = this.activity as Game

        //crea una intercetta dell'evento di quando si preme il tasto "Back" per navigare la
        //schermata nel fragment PAUSA
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //intercetto il pulsante Back
                    view.findNavController().navigate(R.id.action_game_pause_to_game)
                    activity.gameInstance?.reasume()
                }
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_game_pause, container, false)

        val game = this.activity as Game

        val goToGame : Button = view.findViewById(R.id.fragment_pause_go_to_game)
        val goToMenu : Button = view.findViewById(R.id.fragment_pause_go_to_menu)

        goToGame.setOnClickListener {
            val navHost = game.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_game) as NavHostFragment
            navHost.findNavController().navigate(R.id.action_game_pause_to_game)
            game.gameInstance?.reasume()
        }

        //se clicco di andare al menu, lancia l'activity per andare al menu, e uccidi questa
        goToMenu.setOnClickListener {
            val myIntent = Intent(game, MainActivity::class.java)

            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            game.startActivity(myIntent)

            //termina l'activity
            (game as Activity).finish()

            game.gameInstance?.let { it1 -> QuitGameEvent(it1) }
                ?.let { it2 -> game.eventManager.callEvent(it2) }

            game.finish()
        }
        return view
    }
}