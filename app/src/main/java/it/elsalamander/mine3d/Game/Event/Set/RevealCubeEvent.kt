package it.elsalamander.mine3d.Game.Event.Set

import it.elsalamander.mine3d.Game.Event.Manager.Cancellable
import it.elsalamander.mine3d.Game.Event.Manager.EventGame
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Graphic.MineCube


class RevealCubeEvent(instanceGame : GameInstance, var x : Int, var y : Int, var z :Int, var cube : MineCube) : EventGame(instanceGame), Cancellable {

    private var cancelled = false
    /**
     * Imposta questo evento come Cancellato o no.
     */
    override fun setCancellable(cancel: Boolean) {
        cancelled = cancel
    }

    /**
     * Ritorna se l'evento è oppure no cancellato.
     */
    override fun isCancelled(): Boolean {
        return cancelled
    }
}