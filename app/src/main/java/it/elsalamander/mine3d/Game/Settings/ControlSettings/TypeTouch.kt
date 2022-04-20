package it.elsalamander.mine3d.Game.Settings.ControlSettings

/****************************************************************
 * Enumerazione per rapprensentare i vari tipi di Touch nel gioco
 * - Tap
 * - Hold
 * - Niente
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
enum class TypeTouch(var i: Int) {
    Tap(1),
    Hold(2),
    ND(0)
}