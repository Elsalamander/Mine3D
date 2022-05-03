package it.elsalamander.mine3d

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import it.elsalamander.mine3d.Game.Settings.ControlSettings.ControlSettings
import it.elsalamander.mine3d.Game.Settings.ControlSettings.PresetDefault.PresetRevealFlag
import it.elsalamander.mine3d.Game.Settings.JSONManager

/****************************************************************
 * Fragment dei settings di controllo
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class ControlSettingsFragment : Fragment() {

    private var dati : JSONManager? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_control_settings, container, false)

        val default_normal : Button = view.findViewById(R.id.settings_controll_default_normal)
        val default_inverted : Button = view.findViewById(R.id.settings_controll_default_inverted)
        val reveal_sx : Button = view.findViewById(R.id.settings_controll_selecter_reveal_sx)
        val reveal_dx : Button = view.findViewById(R.id.settings_controll_selecter_reveal_dx)
        val reveal_text : TextView = view.findViewById(R.id.settings_control_selecter_reveal_text)
        val flag_sx : Button = view.findViewById(R.id.settings_controll_selecter_flag_sx)
        val flag_dx : Button = view.findViewById(R.id.settings_controll_selecter_flag_dx)
        val flag_text : TextView = view.findViewById(R.id.settings_control_selecter_flag_text)
        var sensivity : SeekBar = view.findViewById(R.id.settings_control_sensivity_bar)
        var hold_time : SeekBar  = view.findViewById(R.id.settings_control_hold_bar)


        //carica i dati dal file JSON
        this.dati = JSONManager(view.context as Activity)
        val sett : ControlSettings = dati?.controlSett!!
        sett.load()

        //imposta i valori recuperati
        reveal_text.text = sett.reveal_sx_dx.getVal().toString()
        flag_text.text = sett.flag_sx_dx.getVal().toString()
        sensivity.progress = (sett.sensivity.getVal()*100).toInt()
        hold_time.progress = (sett.holdTimer.getVal()*100).toInt()


        //imposta gli eventi
        reveal_sx.setOnClickListener(sett.reveal_sx_dx)
        reveal_dx.setOnClickListener(sett.reveal_sx_dx)
        flag_sx.setOnClickListener(sett.flag_sx_dx)
        flag_dx.setOnClickListener(sett.flag_sx_dx)
        default_normal.setOnClickListener(PresetRevealFlag(sett,1, view))
        default_inverted.setOnClickListener(PresetRevealFlag(sett,2, view))

        return view
    }

    override fun onPause() {
        super.onPause()
        dati?.controlSett?.save()
    }

}