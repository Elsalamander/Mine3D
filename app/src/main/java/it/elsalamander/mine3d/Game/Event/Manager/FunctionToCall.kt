package it.elsalamander.mine3d.Game.Event.Manager

import kotlin.reflect.KFunction

/****************************************************************
 * Data Class come supporto all'EventManager
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
data class FunctionToCall(var cl : ListenerGame, var fn : KFunction<*>) : Comparable<FunctionToCall>{

    var ignore : Boolean = (fn.annotations.filterIsInstance<EventHandlerGame>()[0]).ignoreCancelled

    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal
     * to the specified [other] object, a negative number if it's less than [other], or a positive number
     * if it's greater than [other].
     */
    override fun compareTo(other: FunctionToCall): Int {
        val prio_A = (other.fn.annotations.filterIsInstance<EventHandlerGame>()[0]).prio.priority
        val prio_B = (this.fn.annotations.filterIsInstance<EventHandlerGame>()[0]).prio.priority
        return prio_A - prio_B
    }
}
