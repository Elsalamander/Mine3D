package it.elsalamander.mine3d.Game.Event.Listener

import android.util.Log
import it.elsalamander.mine3d.Game.Event.Manager.EventHandlerGame
import it.elsalamander.mine3d.Game.Event.Manager.EventPriority
import it.elsalamander.mine3d.Game.Event.Manager.ListenerGame
import it.elsalamander.mine3d.Game.Event.Set.GameStart

/****************************************************************
 * Classe dove faccio una prima gestione degli eventi NON
 * principali del gioco
 *
 * C'è la soppressione del Lint per quanto riguarda funzioni "unused"
 * poichè queste funzioni sono chiamate in un modo non dichiarato
 * risultando così apparentemente non utilizzate
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v2.0
 ****************************************************************/
class ListenerForSound : ListenerGame {

    /**
     * Gestion dell'evento GameStart
     * Priorità di esecuzione bassa
     * Avvia la musica di sottofondo
     */
    @EventHandlerGame(EventPriority.LOW, true)
    fun onStartGame(event : GameStart){
        //Avvia la musichetta
        //...
    }
}