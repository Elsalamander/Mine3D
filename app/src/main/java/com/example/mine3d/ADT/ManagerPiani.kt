package com.example.mine3d.ADT

import java.util.*

/************************************************************
 *
 * Gestore dei piani e la loro generazione.
 *
 * @author Elsalamander
 * @date 30 set 2021
 * @version 1.0
 ************************************************************/
object ManagerPiani {
    private val piani: MutableMap<String, Piano<*>>

    init{
        piani = TreeMap<String, Piano<*>>()
    }

    /**
     * Ritorna se c'è un piano con associato il nome dato.
     * @param nome
     * @return
     */
    fun contain(nome: String): Boolean {
        return piani.containsKey(nome)
    }

    /**
     * Crea un piano e aggiungilo associandolo al nome dato se
     * e solo se non c'è ne era uno con lo stesso nome.
     * @param <T>
     * @param nome
     * @param dimension
     * @return
    </T> */
    fun <T> add(nome: String, dimension: Byte): Piano<T>? {
        if (!contain(nome)) {
            val tmp = Piano<T>(dimension)
            piani[nome] = tmp
            return tmp
        }
        return piani[nome] as Piano<T>
    }

    /**
     * Rimuovi il piano dato.
     * @param nome
     * @return
     */
    fun remove(nome: String?): Boolean {
        return this.remove(nome)
    }

    /**
     * Ritorna il piano con il nome dato
     * @param nome
     * @return
     */
    fun getPiano(nome: String): Piano<*>? {
        return piani[nome]
    }
}
