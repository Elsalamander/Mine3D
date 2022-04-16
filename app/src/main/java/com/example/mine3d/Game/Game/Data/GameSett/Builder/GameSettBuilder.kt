package com.example.mine3d.Game.Game.Data.GameSett.Builder

import com.example.mine3d.Game.Game.Data.GameSett.Difficulty
import com.example.mine3d.Game.Game.Data.GameSett.GameSett
import com.example.mine3d.util.Builder

class GameSettBuilder : Builder<GameSett>() {

    var size : Int = 0
    var difficulty : Double = Difficulty.EASY.difficulty
    var next : Boolean = false
    var incr : Int = 0


    /**
     * Costruisci l'oggetto
     */
    override fun build(): GameSett {
        return GameSett(size, difficulty, next, incr)
    }
}