package it.elsalamander.mine3d.Game.Game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import it.elsalamander.mine3d.MainActivity
import it.elsalamander.mine3d.R

/****************************************************************
 * Fragment per mostrare il risultato a game finito
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GameEndFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //intercetto il pulsante Back
                    //stai fermo
                }
            })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_game_end, container, false)

        val game = this.activity as Game

        val text        : TextView  = view.findViewById(R.id.fragment_end_view)
        val goToMenu    : Button    = view.findViewById(R.id.fragment_end_go_to_menu)
        val goToBuild   : Button    = view.findViewById(R.id.fragment_end_go_to_build)
        val goNext      : Button    = view.findViewById(R.id.fragment_end_go_next)


        //se clicco di andare al menu, lancia l'activity per andare al menu, e uccidi questa
        goToMenu.setOnClickListener {
            val myIntent = Intent(game, MainActivity::class.java)

            game.startActivity(myIntent)

            //termina l'activity
            game.finish()
        }

        //se clicco di creare un nuovo gameCustom, portami al fragment di BuildCustom
        goToBuild.setOnClickListener {
            val navHost = game.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_game) as NavHostFragment
            navHost.findNavController().navigate(R.id.action_end_game_to_buildGameSett)
        }

        //se clicco su "Next", ripeti il game se non ha incremento altrimenti incrementa
        goNext.setOnClickListener {
            //crea la nuova istanza di gioco
            game.gameInstance = game.gameInstance?.getNextInstance()

            val navHost = game.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_game) as NavHostFragment
            navHost.findNavController().navigate(R.id.action_end_game_to_game)
        }

        if(arguments?.getBoolean("End") == true){
            text.text = "Vittoria!!!"
        }else{
            text.text = "GameOver!!!"
        }

        return view
    }
}