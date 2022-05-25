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
    //var dist: Float = 0f
    //var larghezza : Float = 0f

    fun isBomb() : Boolean {
        return this.value == -1
    }

    /**
     * Ritorna l'id della Texture da visualizzare in accordo con
     * la classe object dove i valori sono:
     * 0 -> nero
     * 1 -> 1
     * 2 -> 2
     * 3 -> 3
     * 4 -> 4
     * 5 -> 5
     * 6 -> 6
     * 7 -> 7
     * 8 -> 8
     * 9 -> Bianco
     * 10-> Bomba
     * 11-> Flag
     */
    fun getTextureID(): Int {
        if(flag){
            return 11
        }

        if(hide){
            return 9
        }
        //se è scoperto mostra
        //se è una bomba
        if(isBomb()){
            return 10
        }

        //se è scoperto e non è una bomba mostra il valore
        return value
    }
}