package com.example.mine3d.Game.Game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mine3d.Game.Game.Data.GameInstance
import com.example.mine3d.Game.Game.Data.GameSett.Builder.*
import com.example.mine3d.Game.Game.Data.GameSett.Difficulty
import com.example.mine3d.R
import com.google.android.material.textfield.TextInputEditText

/****************************************************************
 * Fragment della schermata del build della partita Custom
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GameBuildSettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_build_game_settings, container, false)

        val activity = this.activity as Game

        val inputCell      : TextInputEditText = view.findViewById(R.id.build_game_settings_size_edit_text)
        val viewCellSuBomb : TextView          = view.findViewById(R.id.build_game_settings_bombe_su_celle)
        val bombBar        : SeekBar           = view.findViewById(R.id.build_game_settings_bombe_bar)
        val bombText       : TextView          = view.findViewById(R.id.build_game_settings_bomb_num)
        val easyButton     : Button            = view.findViewById(R.id.build_game_settings_difficulty_easy)
        val mediumButton   : Button            = view.findViewById(R.id.build_game_settings_difficulty_medium)
        val hardButton     : Button            = view.findViewById(R.id.build_game_settings_difficulty_hard)
        val increment      : CheckBox          = view.findViewById(R.id.build_game_settings_next)
        val inputIncrement : TextInputEditText = view.findViewById(R.id.build_game_settings_next_input_text)
        val startGame      : Button            = view.findViewById(R.id.build_game_settings_start)

        val buildGameSett = GameSettBuilder()
        //imposta il builder
        inputCell.setOnEditorActionListener(InputCellEvent(buildGameSett, viewCellSuBomb))
        bombBar.setOnSeekBarChangeListener(BombBarEvent(buildGameSett, viewCellSuBomb, bombText))

        easyButton.setOnClickListener(DifficultyButtonEvent(buildGameSett, viewCellSuBomb, bombText, bombBar, Difficulty.EASY))
        mediumButton.setOnClickListener(DifficultyButtonEvent(buildGameSett, viewCellSuBomb, bombText, bombBar, Difficulty.MEDIUM))
        hardButton.setOnClickListener(DifficultyButtonEvent(buildGameSett, viewCellSuBomb, bombText, bombBar, Difficulty.HARD))

        increment.setOnCheckedChangeListener(IncrementCheckBoxEvent(buildGameSett, inputIncrement))
        inputIncrement.setOnEditorActionListener(InputIncrementEvent(buildGameSett))

        //Evento startGameButton -> fai il Build del GameSettings -> Crea l'istanza di gioco -> passa al fragment GameFragment
        startGame.setOnClickListener {
            activity.gameSett = buildGameSett.build()
            view.findNavController().navigate(R.id.action_game_build_settings_to_game)
        }

        return view
    }
}