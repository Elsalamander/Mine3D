package it.elsalamander.mine3d

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import it.elsalamander.mine3d.Game.Settings.BaseSettings.BaseSettings
import it.elsalamander.mine3d.Game.Settings.BaseSettings.Theme.ThemeList
import it.elsalamander.mine3d.Game.Settings.JSONManager

/****************************************************************
 * Fragment dei settings di base.
 *
 * Impostazioni di Base del gioco.
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class SettingsFragment : Fragment() {

    private var dati : JSONManager? = null

    //private lateinit var title_id    : TextView
    //private lateinit var name_tv     : TextView
    private lateinit var name_input  : EditText
    private lateinit var theme_box   : CheckBox
    private lateinit var timer_box   : CheckBox
    private lateinit var bomb_box    : CheckBox
    //private lateinit var music_title : TextView
    private lateinit var music_bar   : SeekBar
    private lateinit var music_cent  : TextView
    //private lateinit var effect_title: TextView
    private lateinit var effect_bar  : SeekBar
    private lateinit var effect_cent : TextView
    private lateinit var vibbrazione : CheckBox
    private lateinit var control     : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        //Ottieni tutti i gli oggetti grafici
        //title_id     = view.findViewById(R.id.settings_Title)
        //name_tv      = view.findViewById(R.id.settings_Name)
        name_input   = view.findViewById(R.id.settings_Name_Input)
        theme_box    = view.findViewById(R.id.settings_SelectTheme)
        timer_box    = view.findViewById(R.id.settings_Timer)
        bomb_box     = view.findViewById(R.id.settings_bomb)
        //music_title  = view.findViewById(R.id.settings_Music_Title)
        music_bar    = view.findViewById(R.id.settings_Music_Bar)
        music_cent   = view.findViewById(R.id.settings_Music_cent)
        //effect_title = view.findViewById(R.id.settings_Effect_Title)
        effect_bar   = view.findViewById(R.id.settings_Effect_Bar)
        effect_cent  = view.findViewById(R.id.settings_Effect_cent)
        vibbrazione  = view.findViewById(R.id.settings_Vibbrazione)
        control      = view.findViewById(R.id.settings_controll)


        //carica i dati dal file JSON
        this.dati = JSONManager(view.context as Activity)
        val sett : BaseSettings = dati?.baseSett!!

        //imposta i valori recuperati
        name_input.setText(sett.nameUtente.getVal())
        theme_box.isChecked = sett.theme.getVal() == ThemeList.DARK
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