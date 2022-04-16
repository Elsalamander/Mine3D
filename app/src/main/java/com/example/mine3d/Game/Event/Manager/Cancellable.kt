package com.example.mine3d.Game.Event.Manager

/****************************************************************
 * Interfaccia da implementare per rendere cancellabile un evento
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
interface Cancellable {

    /**
     * Imposta questo evento come Cancellato o no.
     */
    fun setCancellable(cancel : Boolean)

    /**
     * Ritorna se l'evento è oppure no cancellato.
     */
    fun isCancelled() : Boolean

}