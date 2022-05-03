package it.elsalamander.mine3d.Game.Game

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import it.elsalamander.mine3d.Game.Event.Listener.Listener
import it.elsalamander.mine3d.Game.Event.Manager.EventManager
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Game.Data.GameSett.GameSett
import it.elsalamander.mine3d.Game.Game.Data.GameSett.StandardGameSett
import it.elsalamander.mine3d.Game.Media.Event.Listener.MediaListener
import it.elsalamander.mine3d.Game.Media.SoundMedia
import it.elsalamander.mine3d.Game.Settings.JSONManager
import it.elsalamander.mine3d.R
import java.io.File


/****************************************************************
 * Activity dove viene realizzato il gioco.
 * E diviso in più fragment a seconda dell'intent che riceve
 * alla'avvio questa activity.
 * I Fragment sono:
 * - CustomGame -> Build GameSettings
 * - Game <- Renderer
 * - Schermata di Pausa
 * - Schermo per vittoria/sconfitta
 *
 * Ci sono 4 fragment, ed a seconda dell'intent si parte
 * dal fragment CustomGame o direttamente dal Game.
 *
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class Game() : AppCompatActivity(){

    companion object{
        const val TAG_INTENT_GAME_TYPE = "GameType"
        const val TAG_INTENT_GAME_RECUPERA = "Recupera"
    }

    var gameInstance : GameInstance? = null
    var gameSett : GameSett? = null
    var eventManager = EventManager
    var gameFragment: GameFragment? = null
    lateinit var settings : JSONManager
    lateinit var media : SoundMedia


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settings = JSONManager(this)
        media = SoundMedia(this)

        //registra eventi
        eventManager.registerEvent(Listener())
        eventManager.registerEvent(MediaListener())

        //prendi il gameSett tramite la stringa passata tramite intent
        val typeGame = intent.getStringExtra(TAG_INTENT_GAME_TYPE)?.let{ StandardGameSett.getFromString(it) }

        if(typeGame == StandardGameSett.GAME_LOAD || (intent.getBooleanExtra(TAG_INTENT_GAME_RECUPERA,false))){
            //carica il gioco
            gameInstance = GameInstance(this, true)
        }else{
            //elimina il file corrente
            val file = File(this.filesDir, GameInstance.pathSettings)
            if(file.exists()){
                file.delete()
            }
            gameSett = typeGame?.gameSettings
        }

        this.setContentView(R.layout.activity_game)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_game) as NavHostFragment?
        val navController = navHost!!.navController

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_graph_game)

        //modifica il Navigator in base al gameSettings recuperato dall'intent
        if(gameSett == null) {
            graph.setStartDestination(R.id.buildGameSett)
        }else{
            graph.setStartDestination(R.id.gamefragment)
        }

        navController.graph = graph
    }

    override fun onPause() {
        super.onPause()
        media.pause()

        //quando viene messo in pausa per rotazione
        intent.putExtra(TAG_INTENT_GAME_RECUPERA, true)
    }

    override fun onResume() {
        super.onResume()
        media.reasume()
    }

    override fun onStop() {
        super.onStop()
        media.stop()
        eventManager.unRegisterAll()
    }
}