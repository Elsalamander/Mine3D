package com.example.mine3d.Game.Game.Data.GameSett.Builder

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.widget.TextView

class InputCellEvent(var builder: GameSettBuilder, var viewCellSuBomb: TextView) : TextView.OnEditorActionListener {
    /**
     * Called when an action is being performed.
     *
     * @param v The view that was clicked.
     * @param actionId Identifier of the action.  This will be either the
     * identifier you supplied, or [ EditorInfo.IME_NULL][EditorInfo.IME_NULL] if being called due to the enter key
     * being pressed.
     * @param event If triggered by an enter key, this is the event;
     * otherwise, this is null.
     * @return Return true if you have consumed the action, else false.
     */
    @SuppressLint("SetTextI18n")
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        try{
            builder.size = v?.text.toString().toInt()
        }catch(e : NumberFormatException){
            builder.size = 5
        }
        val N = builder.size
        val nC = (N*N*2 + N*(N-2)*2 + (N-2)*(N-2)*2)
        val bombe = (nC * builder.difficulty).toInt()
        viewCellSuBomb.text = "$bombe/$nC"

        return true
    }
}