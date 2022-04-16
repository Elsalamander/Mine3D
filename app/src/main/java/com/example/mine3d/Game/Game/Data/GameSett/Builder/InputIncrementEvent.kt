package com.example.mine3d.Game.Game.Data.GameSett.Builder

import android.view.KeyEvent
import android.widget.TextView

class InputIncrementEvent(var builder: GameSettBuilder) : TextView.OnEditorActionListener {
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
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        try{
            builder.incr = v?.text.toString().toInt()
        }catch(e : NumberFormatException){
            builder.incr = 1
        }
        return true
    }
}