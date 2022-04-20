package com.example.mine3d.Game.Game

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.mine3d.Game.Event.Listener.Listener
import com.example.mine3d.Game.Event.Manager.EventManager
import com.example.mine3d.Game.Game.Data.GameInstance
import com.example.mine3d.Game.Game.Data.GameSett.GameSett
import com.example.mine3d.Game.Game.Data.GameSett.StandardGameSett
import com.example.mine3d.R


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
        val TAG_INTENT_GAME_TYPE = "GameType"
    }

    var gameInstance : GameInstance? = null
    lateinit var gameSett : GameSett
    var eventManager = EventManager

    init {
        eventManager.registerEvent(Listener())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //prendi il gameSett tramite la stringa passata tramite intent
        gameSett = intent.getStringExtra(TAG_INTENT_GAME_TYPE)?.let{ StandardGameSett.getFromString(it).gameSettings }!!

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

}