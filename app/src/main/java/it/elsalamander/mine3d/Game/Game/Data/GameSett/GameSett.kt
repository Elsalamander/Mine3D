package it.elsalamander.mine3d.Game.Game.Data.GameSett

import org.json.JSONObject

/****************************************************************
 * Impostazioni del gioco corrente:
 *  - Lato -> N
 *  - Bombe -> Numero bombe
 *  - Next -> concludi/continua (False/True)
 *  - Incremento lato -> (0 o più)
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
data class GameSett(var n: Int, var bomb: Double, var next: Boolean, var incr: Int) {

    companion object{
        const val path_size = "$.GameSett.Size"
        const val path_bomb = "$.GameSett.Bomb"
        const val path_next = "$.GameSett.Next"
        const val path_incr = "$.GameSett.Increment"

        /**
         * Crea e carica i valori dal file JSON
         */
        fun loadFromJSON(json : JSONObject) : GameSett{
            return GameSett(json)
        }
    }

    fun save(json: JSONObject) {
        json.put(path_size, n)
        json.put(path_bomb, bomb)
        json.put(path_next, next)
        json.put(path_incr, incr)
    }

    constructor(json : JSONObject) : this(0,0.0,false,0){
        n    = json.getInt(path_size)
        bomb = json.getDouble(path_bomb)
        next = json.getBoolean(path_next)
        incr = json.getInt(path_incr)
    }

    /**
     * Numero di bombe
     */
    fun numberOfBomb() : Int{
        return (this.n * this.bomb).toInt()
    }

    /**
     * Ritorna il GameSett per il prossimo game
     */
    fun getNextGameSett(): GameSett {
        return GameSett(n+incr, bomb, next, incr)
    }
}