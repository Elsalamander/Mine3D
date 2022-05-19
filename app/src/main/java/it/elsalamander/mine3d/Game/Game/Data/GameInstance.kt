package it.elsalamander.mine3d.Game.Game.Data

import android.content.Context
import android.os.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import it.elsalamander.mine3d.Game.Event.Set.GameStart
import it.elsalamander.mine3d.Game.Game.Data.GameSett.GameSett
import it.elsalamander.mine3d.Game.Game.Game
import it.elsalamander.mine3d.Game.Graphic.Griglia
import it.elsalamander.mine3d.R
import org.json.JSONException
import org.json.JSONObject
import java.io.*


/****************************************************************
 * Oggetto che descive l'istanza di gioco e i suoi step
 * I step sono:
 * - Start
 * - Pause
 * - Reasume
 * - Finish
 *
 * Bisogna salvare lo stato in un file JSON
 * Valori da salvare sono:
 * - GameSett -> Size, Difficulty, Next, Increment
 * - Il valore di ogni cubo -> X, Y, Z, Scovered, Value
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GameInstance(var context: Game, load : Boolean = false) {

    lateinit var grid : Griglia  //griglia di gioco

    companion object{
        const val pathSettings : String = "LastGame.json"
    }

    /**
     * Questo oggetto viene creato quando la partita deve iniziare -> Chiama evento StartGame
     */
    init{
        this.context.gameInstance = this
        if(!load){
            this.grid = Griglia(context.gameSett?.n ?: 5)
        }else{
            this.reasume()
        }
        this.context.eventManager.callEvent(GameStart(this))
    }

    /**
     * Funzione che esegue l'avvio del game
     * Questa funzione viene chiamata tramite evento di "GameStart"
     */
    fun startGame(){

    }

    /**
     * Funzione chiamata quando deve essere messo in pausa il gioco
     */
    fun pause(){
        //salva lo stato corrente
        this.saveState()

        //de alloca la griglia che non mi serve
        this.grid.resetGrid()
    }

    /**
     * Funzione chiamata quando deve essere eseguito il recupero dello stato del game
     */
    fun reasume(){
        //controlla se il file esiste prima
        val file = File(context.filesDir, pathSettings)
        if(!file.exists()){
            //termina questo Reasum
            return
        }
        this.grid = Griglia(0)
        this.loadState()
        this.grid.N = context.gameSett?.n ?: 5
    }

    /**
     * Funzione chiamata quando il game incorre a una fine
     * @param win TRUE se si ha vinto, FALSE altrimenti
     */
    fun finish(win : Boolean){
        //fai vibbrare il telefono se abilitato
        if(context.settings.baseSett.vibbrazione.getVal() && !win){
            val v = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
            }else{
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        }

        val bundle = Bundle()
        bundle.putBoolean("End", win)

        val navHost = context.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_game) as NavHostFragment
        navHost.findNavController().navigate(R.id.action_game_to_end, bundle)

        //elimina il salvataggio
        val file = File(context.filesDir, pathSettings)
        if(file.exists()){
            //elimina il file se esiste
            file.delete()
        }
    }

    /**
     * Crea l'istanza per il prossimo Game
     */
    fun getNextInstance() : GameInstance{
        context.gameSett = context.gameSett?.getNextGameSett()
        return GameInstance(context)
    }

    /**
     * Salva il GameCorrente
     */
    fun saveState(){
        val json = this.getJSON()
        context.gameSett?.save(json)
        this.grid.save(json)

        val userString: String = json.toString()
        val file = File(context.filesDir, pathSettings)
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(userString)
        bufferedWriter.close()
    }

    /**
     * Carica lo stato dal file JSON
     */
    fun loadState(){
        val json = this.getJSON()
        context.gameSett = GameSett.loadFromJSON(json)
        this.grid.load(json)
    }

    /**
     * Prendi il file JSON per salvare lo stato della partita
     * Se non esiste crea un file vuoto.
     */
    private fun getJSON(): JSONObject {
        val file = File(context.filesDir, pathSettings)
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
        return try{
            JSONObject(stringBuilder.toString())
        }catch (e : JSONException){
            JSONObject()
        }
    }

}