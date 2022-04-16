package com.example.mine3d.Game.Settings

import android.app.Activity
import android.content.Context
import com.example.mine3d.Game.Settings.BaseSettings.Theme.ThemeList
import com.example.mine3d.Game.Settings.ControlSettings.TypeTouch
import org.json.JSONException
import org.json.JSONObject

/****************************************************************
 * Interfaccia per dare la struttura minima per ogni impostazione
 * presente nel gioco.
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
abstract class Setting<T>(private val defaultVal: T, protected val context: Activity) {

    protected var value : T = defaultVal

    /**
     * Imposta il valore
     */
    fun setVal(value : T){
        this.value = value
    }

    /**
     * Ritorna il valore dell'impostazione attuale
     */
    fun getVal() : T{
        return this.value
    }

    /**
     * Ritorna il valore di default dell'impostazione
     */
    fun getDefault() : T{
        return this.defaultVal
    }

    /**
     * Carica il valore dato il file
     */
    @Suppress("UNCHECKED_CAST")
    fun load(json : JSONObject){
        try{
            this.value = when(this.value){
                is Boolean -> (json.getBoolean(this.javaClass.name)) as T
                is Int -> (json.getInt(this.javaClass.name)) as T
                is Double -> (json.getDouble(this.javaClass.name)) as T
                is String -> (json.getString(this.javaClass.name)) as T
                is ThemeList -> ThemeList.valueOf(json.getString(this.javaClass.name)) as T
                is TypeTouch -> TypeTouch.valueOf(json.getString(this.javaClass.name)) as T
                else -> this.defaultVal
            }
        }catch (e : JSONException){
            this.value = this.defaultVal
        }
    }

    /**
     * Salva il valore
     */
    fun save(json : JSONObject) : JSONObject{
        return json.put(this.javaClass.name, this.value)
    }
}