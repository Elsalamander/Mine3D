package com.example.mine3d.Game.Settings.BaseSettings.Timer

import android.app.Activity
import android.widget.CompoundButton
import com.example.mine3d.Game.Settings.Setting

/****************************************************************
 *
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class ShowTimer(context : Activity) : Setting<Boolean>(false, context),
    CompoundButton.OnCheckedChangeListener {


    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        this.value = isChecked
    }
}