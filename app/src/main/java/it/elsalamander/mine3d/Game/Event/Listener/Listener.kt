package it.elsalamander.mine3d.Game.Event.Listener

import android.util.Log
import it.elsalamander.mine3d.Game.Event.Manager.EventHandlerGame
import it.elsalamander.mine3d.Game.Event.Manager.EventPriority
import it.elsalamander.mine3d.Game.Event.Manager.ListenerGame
import it.elsalamander.mine3d.Game.Event.Set.*

/****************************************************************
 * Classe dove faccio una prima gestione degli eventi prencipali
 * del gioco
 *
 * C'è la soppressione del Lint per quanto riguarda funzioni "unused"
 * poichè queste funzioni sono chiamate in un modo non dichiarato
 * risultando così apparentemente non utilizzate
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v2.0
 ****************************************************************/
class Listener : ListenerGame {

    /**
     * Gestion dell'evento GameStart
     * Priorità di esecuzione neutrale
     * Ignora eventuali cancellazioni dell'evento, se qualche listener l'ha cancellato
     * prima di eseguire questa chimata
     */
    @EventHandlerGame(EventPriority.NORMAL, true)
    fun onStartGame(event : GameStart){
        //Avvia il game
        event.instanceGame.startGame()
    }

    /**
     * Gestione Evento di quando voglio rivelare un cubo dato
     */
    @EventHandlerGame
    fun onRevealCubeEvent(event : RevealCubeEvent){
        Log.d("Event RevealCubeEvent", "Scopro il cubo")
        //voglio rilevare questo cubo
        //ci sono 2 principali casi:
        // - è una bomba
        // - non è una bomba
        val cube = event.cube

        //prima controlla se il cubo ha la bandiera
        if(cube.flag){
            //termina l'evento
            event.setCancellable(true)
            return
        }

        //controlla se il cubo è gia stato scoperto
        if(!cube.hide){
            //termina l'evento
            event.setCancellable(true)
            return
        }

        //c'è il caso che questo è il primo reveal
        //chiama l'evento di FirstReveal
        if(!event.instanceGame.grid.popolated){
            //chiama l'evento di rivelazione bomba
            event.instanceGame.context.eventManager.callEvent(FirstRevealEvent(event))
            return
        }

        if(cube.isBomb()){
            //chiama l'evento di rivelazione bomba
            event.instanceGame.context.eventManager.callEvent(RevealBombEvent(event))
            event.setCancellable(true)
            return
        }

        //se non è una bomba altrimenti
        //quando rivelo, devo filtrare 2 casi ancora, uguale a 0 o no
        if(cube.value == 0){
            //se è zero devo rivelare tutti i cubi finchè non trovo uno diverso da 0
            //chiama evento MultiReveal
            event.instanceGame.context.eventManager.callEvent(MultiRevealEvent(event))
            return
        }

        //sto rivelando un cubo che non è bomba, non ha valore uguale a 0 e non è il primo.
        cube.hide = false

        //incrementa il numero di cubi scoperti
        event.instanceGame.grid.scovered++

        //controlla se ha vinto
        if(event.instanceGame.grid.toFind == event.instanceGame.grid.scovered){
            event.instanceGame.context.eventManager.callEvent(WinEvent(event))
            event.setCancellable(true)
            return
        }
    }

    /**
     * Gestione evento della prima rivelazione, popola la griglia
     */
    @EventHandlerGame
    fun onFirstReveal(event: FirstRevealEvent){
        //popola la griglia
        Log.d("FirstReveal Event", "Popolo")
        event.instanceGame.grid.popolate(event.upperEvent.x,
                                         event.upperEvent.y,
                                         event.upperEvent.z,
                                         event.instanceGame.context.gameSett)

        //ora che ho popolato devo scoprire che cosa ho voluto scoprire xD
        //rilancio l'evento correlato, ovvero "RevealCubeEvent"
        event.instanceGame.context.eventManager.callEvent(event.upperEvent)
    }

    /**
     * Gestione evento quando si devono rivelare più cubi alla volta
     */
    @EventHandlerGame
    fun onMultiReveal(event : MultiRevealEvent){
        event.instanceGame.grid.multiRevealFrom(event.upperEvent.x.toLong(),
                                                event.upperEvent.y.toLong(),
                                                event.upperEvent.z.toLong())
        //controlla le condizioni di vittoria
        if(event.instanceGame.grid.toFind == event.instanceGame.grid.scovered){
            event.instanceGame.context.eventManager.callEvent(WinEvent(event.upperEvent))
            return
        }
    }

    /**
     * Gestione dell'evento di quando si scopre una bomba
     */
    @EventHandlerGame
    fun onRevealBomb(event : RevealBombEvent){
        //chiama evento di GameOverEvent
        event.instanceGame.context.eventManager.callEvent(GameOverEvent(event))
    }

    /**
     * Gestione dell'evneto per il piazzamento della bandiera
     */
    @EventHandlerGame
    fun onPlaceFlag(event : PlaceFlagEvent){
        Log.d("Event PlaceFlag", "Piazzo bandiera")
        //non posso mettere la bandiera in un cubo gia esplorato!
        if(!event.cube.hide){
            event.instanceGame.context.eventManager.callEvent(CantFlagCubeEvent(event))
            return
        }

        if(event.cube.flag){
            event.cube.flag = false
            event.instanceGame.grid.flagged--
        }else{
            event.cube.flag = true
            event.instanceGame.grid.flagged++
        }
        val gameFrag = event.instanceGame.context.gameFragment
        val toText = (event.instanceGame.context.gameSett?.numberOfBomb()?.minus(event.instanceGame.grid.flagged)).toString()
        gameFrag?.bomb?.text = toText
    }

    @EventHandlerGame
    fun onCantFlagCube(event : CantFlagCubeEvent){
        Log.d("Event CantPlaceFlagCube", "non metto la bandiera")
    }


    /**
     * Gestione evento Win
     */
    @EventHandlerGame
    fun onWinEvent(event : WinEvent){
        //chiama la funzione di game finito dicendo che si ha vinto
        event.instanceGame.finish(true)
    }

    /**
     * Gestione evento GameOver
     */
    @EventHandlerGame
    fun onGameOver(event : GameOverEvent){
        event.instanceGame.finish(false)
    }

    /**
     * Gestisco l'evento nel caso si quitta dal gioco
     */
    @EventHandlerGame
    fun onQuitGame(event : QuitGameEvent){
        //ferma la musica
        event.instanceGame.context.media.stop()
    }

    @EventHandlerGame
    fun onPausedGame(event : PausedGameEvent){
        event.instanceGame.pause()
    }
}