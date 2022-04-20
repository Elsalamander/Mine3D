package it.elsalamander.mine3d.Game.Event.Manager

/****************************************************************
 * Annotazione per le funzioni che realizzano un evento, con
 * parametri di default.
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
annotation class EventHandlerGame(val prio : EventPriority = EventPriority.NORMAL,
                                  val ignoreCancelled : Boolean = false)


