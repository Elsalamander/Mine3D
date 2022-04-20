package it.elsalamander.mine3d.Game.Game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.R

/****************************************************************
 * Fragment dove viene eseguito il rendering del gioco
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GameFragment : Fragment() {

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
        activity.gameInstance = GameInstance(activity)

        return activity.gameInstance!!.render
    }

}