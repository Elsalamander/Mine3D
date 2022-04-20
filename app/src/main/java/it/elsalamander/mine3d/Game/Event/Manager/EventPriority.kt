package it.elsalamander.mine3d.Game.Event.Manager

/****************************************************************
 * Rappresentazione delle priorità
 * HIGHEST > HIGH > NORMAL > LOW > LOWEST
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
enum class EventPriority(val priority : Int) {

    /**
     * Il primo da eseguire
     */
    HIGHEST(0),
    HIGH(1),
    NORMAL(2),
    LOW(3),
    LOWEST(4)
}