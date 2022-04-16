package com.example.mine3d.Game.Game.Data.GameSett.Builder

import android.widget.CompoundButton
import com.google.android.material.textfield.TextInputEditText

class IncrementCheckBoxEvent(var builder: GameSettBuilder, var inputIncrement: TextInputEditText): CompoundButton.OnCheckedChangeListener{
    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        builder.next = isChecked
        try{
           builder.incr = inputIncrement.text.toString().toInt()
        }catch(e : NumberFormatException){
            builder.incr = 1
        }
    }
}