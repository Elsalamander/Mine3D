package it.elsalamander.mine3d

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import it.elsalamander.mine3d.Game.Game.Data.GameSett.StandardGameSett
import it.elsalamander.mine3d.Game.Game.Game

class StandardGameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_standard_game_list, container, false)

        val fiveEasy : Button = view.findViewById(R.id.game_standard_5_easy)
        val fiveMedium : Button = view.findViewById(R.id.game_standard_5_medium)
        val fiveHard : Button = view.findViewById(R.id.game_standard_5_hard)

        val sevenEasy : Button = view.findViewById(R.id.game_standard_7_easy)
        val sevenMedium : Button = view.findViewById(R.id.game_standard_7_medium)
        val sevenHard : Button = view.findViewById(R.id.game_standard_7_hard)


        fiveEasy.setOnClickListener(ClickButton(StandardGameSett.GAME_NORMAL_5_EASY))
        fiveMedium.setOnClickListener(ClickButton(StandardGameSett.GAME_NORMAL_5_MEDIUM))
        fiveHard.setOnClickListener(ClickButton(StandardGameSett.GAME_NORMAL_5_HARD))

        sevenEasy.setOnClickListener(ClickButton(StandardGameSett.GAME_NORMAL_7_EASY))
        sevenMedium.setOnClickListener(ClickButton(StandardGameSett.GAME_NORMAL_7_MEDIUM))
        sevenHard.setOnClickListener(ClickButton(StandardGameSett.GAME_NORMAL_7_HARD))

        return view
    }

    private class ClickButton(val type : StandardGameSett) : View.OnClickListener{
        override fun onClick(v: View?) {
            val myIntent = Intent(v?.context, Game::class.java)
            myIntent.putExtra(Game.TAG_INTENT_GAME_TYPE, type.str)
            v?.context?.startActivity(myIntent)
        }

    }
}