package com.example.mine3d.Game.Settings.BaseSettings.Theme

import com.example.mine3d.Game.Settings.Setting

/****************************************************************
 * Enumerazione dei Themi presenti
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
enum class ThemeList(var id: Int, var desc: String, var red: Int, var green: Int, var blue: Int) {
    DARK(0,  "Tema scuro", 0, 0, 0),
    LIGHT(1, "Tema chiaro",1, 1, 1);
}