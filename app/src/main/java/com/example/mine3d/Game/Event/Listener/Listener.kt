package com.example.mine3d.Game.Event.Listener

import android.util.Log
import com.example.mine3d.Game.Event.Manager.EventHandlerGame
import com.example.mine3d.Game.Event.Manager.EventPriority
import com.example.mine3d.Game.Event.Manager.ListenerGame
import com.example.mine3d.Game.Event.Set.GameStart

class Listener : ListenerGame{

    /**
     * Gestion dell'evento GameStart
     * Priorità di esecuzione neutrale
     * Ignora eventuali cancellazioni dell'evento, se qualche listener l'ha cancellato
     * prima di eseguire questa chimata
     */
    @EventHandlerGame(EventPriority.NORMAL, true)
    fun onStartGame(event : GameStart){
        //Avvia il game
        event.instanceGame.StartGame()
    }
}