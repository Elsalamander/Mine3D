package com.example.mine3d.util

/****************************************************************
 * Classe astratta per realizzare un Builder
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
abstract class Builder<T> {

    /**
     * Costruisci l'oggetto
     */
    abstract fun build() : T
}