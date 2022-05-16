package it.elsalamander.mine3d.Game.Game.Data.GameSett

import java.util.*

/****************************************************************
 * GameSettings standard presenti
 * - Type:Normal, lato: 5, difficoltà: EASY
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
enum class StandardGameSett(var gameSettings : GameSett?, var str: String) {
    GAME_NORMAL_5_EASY(GameSett(5, Difficulty.EASY.difficulty, false, 0), "N-5-E"),
    GAME_NORMAL_5_MEDIUM(GameSett(5, Difficulty.EASY.difficulty, false, 0), "N-5-M"),
    GAME_NORMAL_5_HARD(GameSett(5, Difficulty.EASY.difficulty, false, 0), "N-5-H"),

    GAME_NORMAL_7_EASY(GameSett(7, Difficulty.EASY.difficulty, false, 0), "N-7-E"),
    GAME_NORMAL_7_MEDIUM(GameSett(7, Difficulty.EASY.difficulty, false, 0), "N-7-M"),
    GAME_NORMAL_7_HARD(GameSett(7, Difficulty.EASY.difficulty, false, 0), "N-7-H"),

    GAME_CUSTOM(null, "C"),
    GAME_LOAD(null, "L");

    companion object {
        fun getFromString(str : String) : StandardGameSett{
            val scan = Scanner(str)
            scan.useDelimiter("-")

            //prima lettera
            val ritorna : StandardGameSett = when(scan.next()){
                "N" -> {
                    when(scan.nextInt()){
                        5 -> when(scan.next()){
                            "E" -> GAME_NORMAL_5_EASY
                            "M" -> GAME_NORMAL_5_MEDIUM
                            "H" -> GAME_NORMAL_5_HARD
                            else -> GAME_CUSTOM
                        }

                        7 -> when(scan.next()){
                            "E" -> GAME_NORMAL_7_EASY
                            "M" -> GAME_NORMAL_7_MEDIUM
                            "H" -> GAME_NORMAL_7_HARD
                            else -> GAME_CUSTOM
                        }
                        else -> GAME_CUSTOM
                    }
                }

                "C" -> GAME_CUSTOM
                "L" -> GAME_LOAD
                else -> GAME_CUSTOM
            }

            scan.close()
            return ritorna
        }
    }
}