package com.example.mine3d.Game.Game.Data.GameSett.Builder

import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.example.mine3d.Game.Game.Data.GameSett.Difficulty
import com.example.mine3d.Game.Game.GameBuildSettingsFragment

class DifficultyButtonEvent(var builder: GameSettBuilder, var fragment: GameBuildSettingsFragment, var difficulty: Difficulty) : View.OnClickListener {

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        builder.difficulty = difficulty.difficulty

        fragment.upDateLayout(builder)
    }
}