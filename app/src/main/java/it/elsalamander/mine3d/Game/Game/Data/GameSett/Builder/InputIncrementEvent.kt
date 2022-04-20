package it.elsalamander.mine3d.Game.Game.Data.GameSett.Builder

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import it.elsalamander.mine3d.Game.Game.GameBuildSettingsFragment

class InputIncrementEvent(var builder: GameSettBuilder, var fragment: GameBuildSettingsFragment) : TextView.OnEditorActionListener {
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
    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        fragment.upDateLayout(builder)
        return when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                // Hide keyboard
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                // Give up focus
                v.clearFocus()
                true
            }
            else -> false
        }
    }
}