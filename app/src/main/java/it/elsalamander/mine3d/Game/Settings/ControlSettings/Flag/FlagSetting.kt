package it.elsalamander.mine3d.Game.Settings.ControlSettings.Flag

import android.app.Activity
import android.view.View
import android.widget.TextView
import it.elsalamander.mine3d.Game.Settings.ControlSettings.TypeTouch
import it.elsalamander.mine3d.Game.Settings.Setting
import it.elsalamander.mine3d.R

/****************************************************************
 *
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class FlagSetting(context : Activity) :  Setting<TypeTouch>(TypeTouch.Tap, context), View.OnClickListener{

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        if(v?.id?.equals(R.id.settings_controll_selecter_flag_sx) == true){
            this.value = when(this.value){
                TypeTouch.Hold -> TypeTouch.Tap
                TypeTouch.Tap -> TypeTouch.ND
                TypeTouch.ND -> TypeTouch.Hold
            }
        }

        if(v?.id?.equals(R.id.settings_controll_selecter_flag_dx) == true){
            this.value = when(this.value){
                TypeTouch.Hold -> TypeTouch.ND
                TypeTouch.Tap -> TypeTouch.Hold
                TypeTouch.ND -> TypeTouch.Tap
            }
        }
        val tx : TextView = this.context.findViewById(R.id.settings_control_selecter_flag_text)
        tx.text = this.value.name
    }
}