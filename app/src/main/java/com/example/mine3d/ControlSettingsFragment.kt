package com.example.mine3d

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mine3d.Game.Settings.ControlSettings.ControlSettings
import com.example.mine3d.Game.Settings.ControlSettings.PresetDefault.PresetRevealFlag
import com.example.mine3d.Game.Settings.JSONManager

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

        var default_normal : Button = view.findViewById(R.id.settings_controll_default_normal)
        var default_inverted : Button = view.findViewById(R.id.settings_controll_default_inverted)
        var reveal_sx : Button = view.findViewById(R.id.settings_controll_selecter_reveal_sx)
        var reveal_dx : Button = view.findViewById(R.id.settings_controll_selecter_reveal_dx)
        var reveal_text : TextView = view.findViewById(R.id.settings_control_selecter_reveal_text)
        var flag_sx : Button = view.findViewById(R.id.settings_controll_selecter_flag_sx)
        var flag_dx : Button = view.findViewById(R.id.settings_controll_selecter_flag_dx)
        var flag_text : TextView = view.findViewById(R.id.settings_control_selecter_flag_text)
        var sensivity : SeekBar = view.findViewById(R.id.settings_control_sensivity_bar)
        var hold_time : SeekBar  = view.findViewById(R.id.settings_control_hold_bar)


        //carica i dati dal file JSON
        this.dati = JSONManager(view.context as Activity)
        val sett : ControlSettings = dati?.controlSett!!

        //imposta i valori recuperati
        reveal_text.text = sett.reveal_sx_dx.getVal().toString()
        flag_text.text = sett.flag_sx_dx.getVal().toString()


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