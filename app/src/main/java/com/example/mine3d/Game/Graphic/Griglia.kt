package com.example.mine3d.Game.Graphic

import com.example.mine3d.ADT.*
import org.json.JSONObject


/****************************************************************
 * Classe che descrive la griglia di gioco.
 * E' un insieme di cubi, dove ogni cubo ha delle caratteristiche
 *
 * La griglia è tipo (esempio lato 4)
 *
 *             XXXX
 *             XXXX
 *          XX XXXX XX XXXX
 *          XX XXXX XX XXXX
 *          XX XXXX XX XXXX
 *          XX XXXX XX XXXX
 *             XXXX
 *             XXXX
 *
 * Le coordinate devono essere tali da rappresentarsi nello spazio possibilmente.
 * Idea 1:
 *      Creare 6 matrici NxN, con rindondanza degli spigoli
 *      Essendo i piani del cubo ortogonali le matrice scorrono solo su 2 assi.
 *
 * Idea 2: Creare varie matrici per avere rindondanza degli spigoli, difficile poi
 *         capire date le coordinate che matrice prendere
 *
 * Realizzazione:
 *      - Uso una mia vecchia libreria per mappare gli oggetti in un volume, libreria ElsaLib
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class Griglia(var N : Int) {

    private var managerPiani = ManagerPiani                                               //menager dei piani dell'ADT
    private var volume : Piano<MineCube> = managerPiani.add("Gioco", 3)!!  //Crea un volume
    private var grid : Zona<MineCube> = volume.createZona()                               //Crea una zona nel volume per allocare gli oggetti

    init{
        //metti nel volume "grid" tutti i cubi
        for(x in 0 until N){
            for(y in 0 until N){
                for(z in 0 until N){
                    if(x == 0 || y == 0 || z == 0 || x == N-1 || y == N-1 || z == N-1){
                        val arr = longArrayOf(x.toLong(), y.toLong(), z.toLong())
                        grid.put(Point(arr), MineCube())
                    }
                }
            }
        }
    }

    fun getCubeIn(x: Long, y: Long, z: Long) : MineCube?{
        return grid[Point(longArrayOf(x, y, z))]
    }

    fun visitLeaf(function: (NodeSAH<Pair<Area<MineCube>, MineCube>?>?) -> Unit) {
        grid.visitLeaf(function)
    }

    /**
     * Salva lo stato di tutti i cubi, da salvare sono:
     * - Coordinate X, Y, Z
     * - Lo stato del cubo:
     *      - Reveal: TRUE/FALSE
     *      - Valore: -1 ~ 8
     */
    fun save(json: JSONObject) {
        this.visitLeaf {
            // JSONObject temporaneo
            val tmp = JSONObject()
            tmp.put("$.x", it?.getPoint()?.getAxisValue(0))
            tmp.put("$.y", it?.getPoint()?.getAxisValue(1))
            tmp.put("$.z", it?.getPoint()?.getAxisValue(2))
            tmp.put("$.reveal", it?.getVal()?.second?.hide)
            tmp.put("$.value", it?.getVal()?.second?.value)

            //accoda nell'array JSON
            json.accumulate("$.cubes", tmp)
        }
    }

    /**
     * Carica tutti i valori dal file JSON
     */
    fun load(json : JSONObject){
        val jsonArr = json.getJSONArray("$.cubes")

        (0 until jsonArr.length()).forEach{
            val tmp = jsonArr.getJSONObject(it)

            val x = tmp.getLong("$.x")
            val y = tmp.getLong("$.y")
            val z = tmp.getLong("$.z")
            val reveal = tmp.getBoolean("$.reveal")
            val value  = tmp.getInt("$.value")
            val arr = longArrayOf(x, y, z)
            grid.put(Point(arr), MineCube(value, reveal))
        }

    }

}