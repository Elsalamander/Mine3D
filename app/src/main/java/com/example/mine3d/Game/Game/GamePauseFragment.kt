package com.example.mine3d.Game.Game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.mine3d.MainActivity
import com.example.mine3d.R

/****************************************************************
 * Fragment dove è descritta la schermata di pausa
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GamePauseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_game_pause, container, false)

        val game = this.activity as Game

        val goToGame : Button = view.findViewById(R.id.fragment_pause_go_to_game)
        val goToMenu : Button = view.findViewById(R.id.fragment_pause_go_to_menu)

        goToGame.setOnClickListener {
            val navHost = game.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_game) as NavHostFragment
            navHost.findNavController().navigate(R.id.action_game_pause_to_game)
        }

        //se clicco di andare al menu, lancia l'activity per andare al menu, e uccidi questa
        goToMenu.setOnClickListener {
            val myIntent = Intent(game, MainActivity::class.java)

            game.startActivity(myIntent)

            //termina l'activity
            game.finish()
        }

        return view
    }
}