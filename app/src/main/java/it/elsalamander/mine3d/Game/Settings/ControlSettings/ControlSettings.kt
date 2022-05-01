package it.elsalamander.mine3d.Game.Settings.ControlSettings

import android.app.Activity
import it.elsalamander.mine3d.Game.Settings.ControlSettings.Flag.FlagSetting
import it.elsalamander.mine3d.Game.Settings.ControlSettings.HoldTimer.HoldTimer
import it.elsalamander.mine3d.Game.Settings.ControlSettings.Reveal.RevealSetting
import it.elsalamander.mine3d.Game.Settings.ControlSettings.Sensivity.Sensivity
import it.elsalamander.mine3d.Game.Settings.GenericSettings
import org.json.JSONException
import org.json.JSONObject
import java.io.*

/****************************************************************
 * Settings dei controlli
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class ControlSettings(var context : Activity) : GenericSettings {

    var reveal_sx_dx = RevealSetting(context)
    var flag_sx_dx   = FlagSetting(context)
    var holdTimer    = HoldTimer(context)
    var sensivity    = Sensivity(context)

    companion object{
        private var pathSettings : String = "Settings.json"
    }

    /**
     * Carica il file e leggi tutti i valori
     */
    init{
        val json = this.getJSON()
        this.load(json)
    }

    /**
     * Prendi il file JSON
     */
    override fun getJSON(): JSONObject {
        val file = File(context.filesDir, ControlSettings.pathSettings)
        if(!file.exists()){
            //il file non esiste
            file.createNewFile()
        }
        val fileReader = FileReader(file)
        val bufferedReader = BufferedReader(fileReader)
        val stringBuilder = StringBuilder()
        var line: String? = bufferedReader.readLine()
        while (line != null) {
            stringBuilder.append(line).append("\n")
            line = bufferedReader.readLine()
        }
        bufferedReader.close()
        try{
            return JSONObject(stringBuilder.toString())
        }catch (e : JSONException){
            val json = JSONObject()
            this.save(json)
            return json
        }
    }

    /**
     * Carica i dati dal file JSON
     */
    override fun load(json: JSONObject) {
        this.reveal_sx_dx.load(json)
        this.flag_sx_dx.load(json)
        this.holdTimer.load(json)
        //...


    }

    /**
     * Salva i dati nel file JSON
     */
    override fun save(json: JSONObject) {
        this.reveal_sx_dx.save(json)
        this.flag_sx_dx.save(json)
        this.holdTimer.save(json)
        //...


        val userString: String = json.toString()
        val file = File(context.filesDir, ControlSettings.pathSettings)
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(userString)
        bufferedWriter.close()
    }
}