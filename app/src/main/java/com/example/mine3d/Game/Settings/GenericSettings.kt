package com.example.mine3d.Game.Settings

import org.json.JSONObject

/****************************************************************
 * Interfaccia per descrivere come deve essere un contenitore
 * dei settings
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
interface GenericSettings {

    /**
     * Prendi il file JSON
     */
    fun getJSON(): JSONObject;

    /**
     * Carica i dati dal file JSON
     */
    fun load(json: JSONObject = this.getJSON())

    /**
     * Salva i dati nel file JSON
     */
    fun save(json : JSONObject = this.getJSON())

}