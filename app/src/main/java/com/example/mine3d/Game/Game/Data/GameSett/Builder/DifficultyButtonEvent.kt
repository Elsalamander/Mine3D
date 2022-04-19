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

        val N = builder.size
        val nC = (N*N*2 + N*(N-2)*2 + (N-2)*(N-2)*2)
        val bombe = (nC * builder.difficulty).toInt()
        viewCellSuBomb.text = "$bombe/$nC"

        val cent = (difficulty.difficulty*100).toInt()
        bombText.text = "$cent%"

        bar.progress = cent
    }
}