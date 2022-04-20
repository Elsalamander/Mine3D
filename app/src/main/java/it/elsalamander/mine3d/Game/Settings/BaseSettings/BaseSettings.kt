package it.elsalamander.mine3d.Game.Settings.BaseSettings

import android.app.Activity
import it.elsalamander.mine3d.Game.Settings.BaseSettings.Bomb.ShowBomb
import it.elsalamander.mine3d.Game.Settings.BaseSettings.Effect.EffectLevel
import it.elsalamander.mine3d.Game.Settings.BaseSettings.Music.MusicLevel
import it.elsalamander.mine3d.Game.Settings.BaseSettings.Theme.Theme
import it.elsalamander.mine3d.Game.Settings.BaseSettings.Timer.ShowTimer
import it.elsalamander.mine3d.Game.Settings.BaseSettings.UserName.UserName
import it.elsalamander.mine3d.Game.Settings.BaseSettings.Vibbrazione.Vibbrazione
import it.elsalamander.mine3d.Game.Settings.GenericSettings
import org.json.JSONException
import org.json.JSONObject
import java.io.*

/****************************************************************
 * Impostazioni di base.
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class BaseSettings(var context : Activity) : GenericSettings {
    var nameUtente  : UserName      = UserName(context)     //nome utente
    var theme       : Theme         = Theme(context)        //tema sfondo
    var showTimer   : ShowTimer     = ShowTimer(context)    //Mostra il timer o no
    var showBomb    : ShowBomb      = ShowBomb(context)     //Mostra le bombe rimanenti
    var musicLevel  : MusicLevel    = MusicLevel(context)   //Livello musica
    var effectLevel : EffectLevel   = EffectLevel(context)  //Livello effetti
    var vibbrazione : Vibbrazione   = Vibbrazione(context)  //Vibbrazione al GameOverEvent

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
     * Ottieni l'oggetto JSON
     */
    override fun getJSON(): JSONObject {
        val file = File(context.filesDir, pathSettings)
        if(!file?.exists()){
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
     * Leggi i dati del file.json
     */
    override fun load(json: JSONObject) {
        this.nameUtente.load(json)
        this.theme.load(json)
        this.showTimer.load(json)
        this.showBomb.load(json)
        this.musicLevel.load(json)
        this.effectLevel.load(json)
        this.vibbrazione.load(json)
    }

    override fun save(json : JSONObject){
        this.nameUtente.save(json)
        this.theme.save(json)
        this.showTimer.save(json)
        this.showBomb.save(json)
        this.musicLevel.save(json)
        this.effectLevel.save(json)
        this.vibbrazione.save(json)

        val userString: String = json.toString()
        val file = File(context.filesDir, pathSettings)
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(userString)
        bufferedWriter.close()
    }

}