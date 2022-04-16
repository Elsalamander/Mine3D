package com.example.mine3d

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mine3d.Game.Settings.BaseSettings.BaseSettings
import com.example.mine3d.Game.Settings.BaseSettings.Theme.ThemeList
import com.example.mine3d.Game.Settings.JSONManager
import com.google.android.material.textfield.TextInputEditText

/****************************************************************
 * Fragment dei settings di base
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class SettingsFragment : Fragment() {

    private var dati : JSONManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        //Ottieni tutti i gli oggetti grafici
        val title_id    : TextView  = view.findViewById(R.id.settings_Title)
        val name_tv     : TextView  = view.findViewById(R.id.settings_Name)
        val name_input  : TextInputEditText = view.findViewById(R.id.settings_Name_Input)
        val theme_box   : CheckBox  = view.findViewById(R.id.settings_SelectTheme)
        val timer_box   : CheckBox  = view.findViewById(R.id.settings_Timer)
        val bomb_box    : CheckBox  = view.findViewById(R.id.settings_bomb)
        val music_title : TextView  = view.findViewById(R.id.settings_Music_Title)
        val music_bar   : SeekBar   = view.findViewById(R.id.settings_Music_Bar)
        val music_cent  : TextView  = view.findViewById(R.id.settings_Music_cent)
        val effect_title: TextView  = view.findViewById(R.id.settings_Effect_Title)
        val effect_bar  : SeekBar   = view.findViewById(R.id.settings_Effect_Bar)
        val effect_cent : TextView  = view.findViewById(R.id.settings_Effect_cent)
        val vibbrazione : CheckBox  = view.findViewById(R.id.settings_Vibbrazione)
        val control     : Button    = view.findViewById(R.id.settings_controll)



        //carica i dati dal file JSON
        this.dati = JSONManager(view.context as Activity)
        val sett : BaseSettings = dati?.baseSett!!

        //imposta i valori recuperati
        name_input.hint = sett.nameUtente.getVal()
        theme_box.isChecked = (sett.theme.getVal().equals(ThemeList.DARK))
        timer_box.isChecked = sett.showTimer.getVal()
        bomb_box.isChecked  = sett.showBomb.getVal()
        music_bar.progress  = sett.musicLevel.getVal()
        "${sett.musicLevel.getVal()}%".also { music_cent.text = it }
        effect_bar.progress = sett.effectLevel.getVal()
        "${sett.effectLevel.getVal()}%".also { effect_cent.text = it }
        vibbrazione.isChecked = sett.vibbrazione.getVal()

        //imposta eventi associati ai elementView
        //evento click per il pulsante Control
        control.setOnClickListener {
            view.findNavController().navigate(R.id.action_settings_to_control)
        }

        //evento per le Bar, music e effect
        music_bar.setOnSeekBarChangeListener(sett.musicLevel)
        effect_bar.setOnSeekBarChangeListener(sett.effectLevel)

        //eventi Box
        theme_box.setOnCheckedChangeListener(sett.theme)
        bomb_box.setOnCheckedChangeListener(sett.showBomb)
        timer_box.setOnCheckedChangeListener(sett.showTimer)
        vibbrazione.setOnCheckedChangeListener(sett.vibbrazione)

        //evento insert insertTextView
        name_input.setOnEditorActionListener(sett.nameUtente)

        return view
    }

    override fun onPause() {
        super.onPause()
        dati?.baseSett?.save()
    }

}