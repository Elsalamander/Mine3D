package com.example.mine3d.Game.Event.Set

import com.example.mine3d.Game.Event.Manager.Cancellable
import com.example.mine3d.Game.Event.Manager.EventGame
import com.example.mine3d.Game.Game.Data.GameInstance

/****************************************************************
 * Oggetto evento da lanciare quando viene avviato un game.
 * Le informazioni da passare sono:
 * - GameInstance -> contenuta nella classe astratta EventGame
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GameStart(instanceGame : GameInstance) : EventGame(instanceGame), Cancellable {

    var cancelled = false

    /**
     * Imposta questo evento come Cancellato o no.
     */
    override fun setCancellable(cancel: Boolean) {
        this.cancelled = cancel
    }

    /**
     * Ritorna se l'evento è oppure no cancellato.
     */
    override fun isCancelled(): Boolean {
        return this.cancelled
    }
}