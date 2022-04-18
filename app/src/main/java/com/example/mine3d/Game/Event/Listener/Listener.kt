package com.example.mine3d.Game.Event.Listener

import android.util.Log
import com.example.mine3d.Game.Event.Manager.EventHandlerGame
import com.example.mine3d.Game.Event.Manager.EventPriority
import com.example.mine3d.Game.Event.Manager.ListenerGame
import com.example.mine3d.Game.Event.Set.*

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

    /**
     * Gestione Evento di quando voglio rivelare un cubo dato
     */
    @EventHandlerGame
    fun onRevealCubeEvent(event : RevealCubeEvent){
        //voglio rilevare questo cubo
        //ci sono 2 principali casi:
        // - è una bomba
        // - non è una bomba
        val cube = event.cube

        //prima controlla se il cubo ha la bandiera
        if(cube.flag){
            //termina l'evento
            return
        }

        if(cube.isBomb()){
            //chiama l'evento di rivelazione bomba
            event.instanceGame.context.eventManager.callEvent(RevealBombEvent(event))
            return
        }
        //se non è una bomba altrimenti
        //c'è il caso che questo è il primo reveal
        //chiama l'evento di FirstReveal
        if(!event.instanceGame.grid.popolated){
            //chiama l'evento di rivelazione bomba
            event.instanceGame.context.eventManager.callEvent(FirstRevealEvent(event))
            return
        }

        //quando rivelo, devo filtrare 2 casi ancora, uguale a 0 o no
        if(cube.value == 0){
            //se è zero devo rivelare tutti i cubi finchè non trovo uno diverso da 0
            //chiama evento MultiReveal
            event.instanceGame.context.eventManager.callEvent(MultiRevealEvent(event))
            return
        }

        //sto rivelando un cubo che non è bomba, non ha valore uguale a 0 e non è il primo.
        cube.hide = false
    }

    /**
     * Gestione evento della prima rivelazione, popola la griglia
     */
    @EventHandlerGame
    fun onFirstReveal(event: FirstRevealEvent){
        //popola la griglia
        event.instanceGame.grid.popolate(event.upperEvent.x,
                                         event.upperEvent.y,
                                         event.upperEvent.z,
                                         event.instanceGame.sett)
    }

    /**
     * Gestione evento quando si devono rivelare più cubi alla volta
     */
    @EventHandlerGame
    fun onMultiReveal(event : MultiRevealEvent){
        event.instanceGame.grid.multiRevealFrom(event.upperEvent.x.toLong(),
                                                event.upperEvent.y.toLong(),
                                                event.upperEvent.z.toLong())
    }

    /**
     * Gestione dell'evento di quando si scopre una bomba
     */
    @EventHandlerGame
    fun onRevealBomb(event : RevealBombEvent){
        //chiama evento di GameOver
        event.instanceGame.context.eventManager.callEvent(GameOver(event))
    }

    /**
     * Gestione dell'evneto per il piazzamento della bandiera
     */
    @EventHandlerGame
    fun onPlaceFlag(event : PlaceFlagEvent){
        event.cube.flag = !event.cube.flag
    }

}