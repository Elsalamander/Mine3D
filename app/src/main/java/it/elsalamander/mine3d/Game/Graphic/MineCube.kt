package it.elsalamander.mine3d.Game.Graphic

/****************************************************************
 * Classe per la realizzazione del cubo di gioco, non a livello grafico.
 * Ogni cubo ha:
 * - Valore: 1,2,3,4,5,6,7,8; 0 e -1 (lo zero se non ha nulla vicino, -1 se è una bomba)
 * - Scoperto o no, booleano
 * - Una terna di coordinate (x,y,z)
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class MineCube(var value : Int = 0, var hide : Boolean = true) {

    var flag : Boolean = false
    var xRend : Float = 0f
    var yRend : Float = 0f
    var zRend : Float = 0f
    var dist: Float = 0f

    var glCube : GLCubeBase = GLCubeBase

    fun isBomb() : Boolean {
        return this.value == -1
    }

    fun hit(x : Float, y : Float) : Boolean{
        return true
    }
}