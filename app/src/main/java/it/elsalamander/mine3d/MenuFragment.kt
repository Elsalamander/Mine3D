package it.elsalamander.mine3d

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import it.elsalamander.mine3d.Game.Game.Data.GameSett.StandardGameSett
import it.elsalamander.mine3d.Game.Game.Game

/****************************************************************
 * Fragment del menu, il primo fragment visualizzato all'apertura
 * dell'app.
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class MenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        val settButton = view.findViewById<Button>(R.id.sett)
        settButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_menu_to_settings)
        }

        val gameButton = view.findViewById<Button>(R.id.game)
        gameButton.setOnClickListener {
            //crea un Intent
            val myIntent : Intent = Intent(view.context, Game::class.java)

            myIntent.putExtra(Game.TAG_INTENT_GAME_TYPE, StandardGameSett.GAME_NORMAL_5_EASY.str)
            //invia l'intent
            this.startActivity(myIntent)
        }

        return view
    }

}