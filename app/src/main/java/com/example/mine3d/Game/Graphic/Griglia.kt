package com.example.mine3d.Game.Graphic

import com.example.mine3d.ADT.*
import com.example.mine3d.Game.Game.Data.GameSett.GameSett
import org.json.JSONObject
import java.security.SecureRandom


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

    var popolated : Boolean = false    //la griglia è stata popolata
    var scovered  : Int = 0
    var flagged   : Int = 0

    val toFind : Int = (N*N*2 + N*(N-2)*2 + (N-2)*(N-2)*2)

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

    /**
     * Popola la griglia, a partire dal cubo di coordinate X,Y,Z passate
     * che deve essere != -1
     * e con un numero di bombe fate da GameSettings
     */
    fun popolate(x : Int, y : Int, z : Int, gameSett : GameSett){
        val random = SecureRandom()
        val countBomb = gameSett.numberOfBomb()

        //popola prima con le bombe
        this.populateBomb(x,y,z,random,gameSett,countBomb)

        //con le bombe piazzate ora metti i numeri
        this.grid.visitLeaf {
            val cube = it?.getVal()?.second

            if(cube?.isBomb() == false){
                //mi interessa
                //guarda attorno cosa c'è
                val cx = it.getPoint().getAxisValue(0)
                val cy = it.getPoint().getAxisValue(1)
                val cz = it.getPoint().getAxisValue(2)

                var bPresenti : Int = 0

                for(xR in -1 until 2){
                    for(yR in -1 until 2){
                        for(zR in -1 until 2){
                            if(this.getCubeIn(cx+xR,cy+yR,cz+zR)?.isBomb() == true) {
                                bPresenti++
                            }
                        }
                    }
                }
                cube.value = bPresenti
            }
        }
        this.popolated = true
    }

    private fun populateBomb(x: Int, y: Int, z:Int, random : SecureRandom, gameSett : GameSett, countBomb : Int){
        var nBomb = countBomb
        this.grid.visitLeaf {
            //prima controllo se è la foglia da escudere
            if((it?.getPoint()?.getAxisValue(0) == x.toLong()) and (it?.getPoint()?.getAxisValue(1) == y.toLong()) and (it?.getPoint()?.getAxisValue(2) == z.toLong())){
                //skippa questo cubo
            }else{
                //per ogni foglia, genera un numero da 0 a 100 se è superiore a bomb*100 setta come boma
                if((random.nextInt(101) > gameSett.bomb*100) and (it?.getVal()?.second?.isBomb() == false)){
                    //imposta come bomba
                    it?.getVal()?.second?.value = -1
                    //decrementa contatore
                    nBomb--
                }
            }
        }
        if(countBomb > 0){
            this.populateBomb(x,y,z,random,gameSett,nBomb)
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

    fun multiRevealFrom(x: Long, y: Long, z: Long) {
        //devo prendere i cubi vicini
        // - se sono scoperti
        // - prendi il valore che hanno
        //      - se il valore è 0 continua
        //      - se il valore è diverso da 0 fermati
        // - scoprilo
        for(xR in -1 until 2){
            for(yR in -1 until 2){
                for(zR in -1 until 2){
                    val cube = this.getCubeIn(x+xR,y+yR,z+zR)
                    if(cube?.hide == true){
                        //controlla se ha la bandiera
                        if(cube.flag){
                            //non scoprire e non propagare
                            continue
                        }
                        cube.hide = false

                        //incrementa il numero di cubi scoperti
                        this.scovered++
                        if(cube.value == 0) {
                            this.multiRevealFrom(x+xR,y+yR,z+zR)
                        }
                    }
                }
            }
        }
    }

}