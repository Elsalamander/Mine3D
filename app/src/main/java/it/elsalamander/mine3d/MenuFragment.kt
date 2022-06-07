package it.elsalamander.mine3d

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Game.Data.GameSett.StandardGameSett
import it.elsalamander.mine3d.Game.Game.Game
import java.io.File


/****************************************************************
 * Fragment del menu, il primo fragment visualizzato all'apertura
 * dell'app.
 *
 * Menu dove sono presenti 4 pulsanti:
 * - 1: Navigare nel fragment per la selezione del gameStandard.
 * - 2: Passare all'activity 2 per creare il custom Game
 * - 3: Recuperare il game precedente e avviarlo se c'è
 * - 4: Navigare nel fragment delle Impostazioni.
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class MenuFragment : Fragment() {

    companion object{
        //Messaggio del Toast se non c'è un game incompleto
        const val sendError = "Non c'è un game incompleto da caricare!"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        //button per le impostazioni
        val settButton = view.findViewById<Button>(R.id.sett)
        settButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_menu_to_settings)
        }

        //button per i standard game
        val gameButton = view.findViewById<Button>(R.id.game_standard)
        gameButton.setOnClickListener{
            //apri il fragment di selezione game
            view.findNavController().navigate(R.id.action_menu_to_standard_game)
        }

        //button per il custom game
        val gameCustomButton = view.findViewById<Button>(R.id.game_custom)
        gameCustomButton.setOnClickListener {
            //crea un Intent
            val myIntent = Intent(view.context, Game::class.java)

            myIntent.putExtra(Game.TAG_INTENT_GAME_TYPE, StandardGameSett.GAME_CUSTOM.str)

            //invia l'intent
            this.startActivity(myIntent)
        }

        //button per il recupero del game precedente
        val gameReasume = view.findViewById<Button>(R.id.game_reasum)
        gameReasume.setOnClickListener {
            //vedi se esiste il file
            val file = File(context?.filesDir, GameInstance.pathLastGame)
            if(!file.exists()){
                //il file non esiste, manda un messaggio che non c'è un game da recuperare
                val msg = Toast.makeText(this.activity, sendError, Toast.LENGTH_SHORT)
                msg.show()
            }else{
                //c'è un file lancia l'activity
                val myIntent = Intent(view.context, Game::class.java)

                myIntent.putExtra(Game.TAG_INTENT_GAME_TYPE, StandardGameSett.GAME_LOAD.str)

                //invia l'intent
                this.startActivity(myIntent)
            }
        }

        return view
    }

}