package com.example.mine3d.Game.Game.Data.GameSett.Builder

import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.example.mine3d.Game.Game.Data.GameSett.Difficulty

class DifficultyButtonEvent(var builder: GameSettBuilder, var viewCellSuBomb: TextView, var bombText : TextView,
                            var bar : SeekBar, var difficulty: Difficulty) : View.OnClickListener {

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        builder.difficulty = difficulty.difficulty

        val bombe = (builder.size * builder.difficulty).toInt()
        viewCellSuBomb.text = "$bombe/${builder.size}"

        val cent = (difficulty.difficulty*100).toInt()
        bombText.text = "$cent%"

        bar.progress = cent
    }
}