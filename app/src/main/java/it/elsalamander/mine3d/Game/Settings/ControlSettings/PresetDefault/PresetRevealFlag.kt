package it.elsalamander.mine3d.Game.Settings.ControlSettings.PresetDefault

import android.view.View
import android.widget.TextView
import it.elsalamander.mine3d.Game.Settings.ControlSettings.ControlSettings
import it.elsalamander.mine3d.Game.Settings.ControlSettings.TypeTouch
import it.elsalamander.mine3d.R

/****************************************************************
 * Non è un oggetto Settings dato che non ha memoria ma solo
 * azione riguardo i settings stessi
 * Type:
 *      1 - Default
 *      2 - Inverted
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class PresetRevealFlag(var controlSett : ControlSettings, var type : Int, var view : View) : View.OnClickListener{
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        if(this.type == 1){
            this.controlSett.reveal_sx_dx.setVal(TypeTouch.Hold)
            this.controlSett.flag_sx_dx.setVal(TypeTouch.Tap)
        }else if(this.type == 2){
            this.controlSett.reveal_sx_dx.setVal(TypeTouch.Tap)
            this.controlSett.flag_sx_dx.setVal(TypeTouch.Hold)
        }

        this.view.findViewById<TextView>(R.id.settings_control_selecter_reveal_text).text = this.controlSett.reveal_sx_dx.getVal().name
        this.view.findViewById<TextView>(R.id.settings_control_selecter_flag_text).text = this.controlSett.flag_sx_dx.getVal().name
    }
}