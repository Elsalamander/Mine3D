package com.example.mine3d.Game.Game.Data.GameSett

/****************************************************************
 * Livello di difficoltà standard
 * - EASY: 20% di bombe
 * - MEDIUM: 40% di bombe
 * - HARD: 60% di bombe
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
enum class Difficulty(var difficulty : Double) {
    EASY(0.20),
    MEDIUM(0.4),
    HARD(0.6)
}