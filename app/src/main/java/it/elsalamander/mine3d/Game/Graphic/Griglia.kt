package it.elsalamander.mine3d.Game.Graphic

import it.elsalamander.mine3d.ADT.*
import it.elsalamander.mine3d.util.Pair
import it.elsalamander.mine3d.Game.Game.Data.GameSett.GameSett
import org.json.JSONObject
import java.security.SecureRandom
import java.util.ArrayList


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
 * Le coordinate devono essere tali da rappresentarsi nello spazio
 * possibilmente.
 * Idea 1:
 *      Creare 6 matrici NxN, con rindondanza degli spigoli
 *      Essendo i piani del cubo ortogonali le matrice scorrono solo
 *      su 2 assi.
 *
 * Idea 2: Creare varie matrici per avere rindondanza degli spigoli,
 *         difficile poi capire date le coordinate che matrice prendere
 *
 * Realizzazione:
 *      - Uso una mia vecchia libreria per mappare gli oggetti in un
 *        volume, libreria ElsaLib
 *      - Non è stata importata ma parzialmente ri-realizzata in kotlin
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v2.0
 ****************************************************************/
class Griglia(var N : Int) {

    private var managerPiani = ManagerPiani                                               //menager dei piani dell'ADT
    private var volume : Piano<MineCube> = managerPiani.add("Gioco", 3)!!  //Crea un volume
    private var grid : Zona<MineCube> = volume.createZona()                               //Crea una zona nel volume per allocare gli oggetti

    var popolated : Boolean = false    //la griglia è stata popolata
    var scovered  : Int = 0
    var flagged   : Int = 0

    var toFind : Int = (N*N*2 + N*(N-2)*2 + (N-2)*(N-2)*2)

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
    fun popolate(x : Int, y : Int, z : Int, gameSett : GameSett?){
        val random = SecureRandom()
        val countBomb = gameSett!!.numberOfBomb()

        toFind -= countBomb

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

                var bPresenti = 0

                //predi la lista di quelli attorno
                val near = this.getNear(cx, cy, cz)

                //cicla e popola
                for(nr in near){
                    if(nr.isBomb()){
                        bPresenti++
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
                if((random.nextInt(101) < gameSett.bomb*100) and (it?.getVal()?.second?.isBomb() == false) and (nBomb > 0)){
                    //imposta come bomba
                    it?.getVal()?.second?.value = -1
                    //decrementa contatore
                    nBomb--
                }
            }
        }
        if(nBomb > 0){
            this.populateBomb(x,y,z,random,gameSett,nBomb)
        }
    }

    private fun getCubeIn(x: Long, y: Long, z: Long) : MineCube?{
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
        val near = this.getNearPair(x,y,z)
        val coord = LongArray(3)
        coord[0] = x
        coord[0] = y
        coord[0] = z
        val centre : MineCube = this.getCubeIn(x,y,z)!!
        near.add(Pair(coord, centre))

        for(curr in near){
            if(curr.second.hide){
                //controlla se ha la bandiera
                if(curr.second.flag){
                    //non scoprire e non propagare
                    continue
                }

                //scopri
                curr.second.hide = false

                //incrementa il numero di cubi scoperti
                this.scovered++
                if(curr.second.value == 0) {
                    this.multiRevealFrom(curr.first[0],curr.first[1],curr.first[2])
                }
            }
        }
    }

    /**
     * Resetta la griglia attuale
     */
    fun resetGrid() {
        this.scovered = 0
        this.flagged = 0
        this.popolated = false
        this.grid.removeAll()
    }

    /**
     * Ritorna i otto cubi vicini secondo le regole del gioco
     */
    private fun getNearPair(x : Long, y : Long, z : Long) : ArrayList<Pair<LongArray,MineCube>>{
        val arr = ArrayList<Pair<LongArray,MineCube>>()

        //prendo tutti quelli attorno di raggio 1 cubo
        //poi filtro il risultato con quelli che mi interessano.
        for(xR in -1 until 2){
            for(yR in -1 until 2){
                for(zR in -1 until 2){
                    val nx = x+xR.toLong()
                    val ny = y+yR.toLong()
                    val nz = z+zR.toLong()
                    val coords = LongArray(3)
                    coords[0] = nx
                    coords[1] = ny
                    coords[2] = nz
                    if(nx == x && ny == y && nz == z){
                        continue
                    }
                    this.getCubeIn(nx,ny,nz)?.let { arr.add(Pair(coords, it)) }
                }
            }
        }
        //ora filtra il risultato
        val filt = ArrayList<Pair<LongArray,MineCube>>()

        //devo aggiungere i cubi che hanno almeno 1 coordinata in comune
        //se fa parte del telaio
        val nX = LongArray(3)  //numeri di cubi nello stesso asse x
        val nY = LongArray(3)  //numeri di cubi nello stesso asse y
        val nZ = LongArray(3)  //numeri di cubi nello stesso asse z
        for(curr in arr){
            if(curr.first[0] == x){
                filt.add(curr)
                nX[(1+curr.first[0]-x).toInt()] = nX[(1+curr.first[0]-x).toInt()]+1
            }else if(curr.first[2] == z){
                filt.add(curr)
                nZ[(1+curr.first[2]-z).toInt()] = nZ[(1+curr.first[2]-z).toInt()]+1
            }else if(curr.first[1] == y){
                filt.add(curr)
                nY[(1+curr.first[1]-y).toInt()] = nY[(1+curr.first[1]-y).toInt()]+1
            }
        }

        //controllo se sono 7 o 9
        if(filt.size > 8){
            //devo ulteriormente filtrare un caso
            //per poterlo fare uso l'asse più usato
            val maxX = nX.maxOrNull()
            val maxY = nY.maxOrNull()
            val maxZ = nZ.maxOrNull()
            if (maxX != null && maxY != null && maxZ != null) {
                if(maxX > maxY && maxX > maxZ){
                    //la X è la più usata, elimina tutti quelli che hanno una X diversa
                    var id = 0
                    while(id < filt.size){
                        if(filt[id].first[0] != x){
                            filt.removeAt(id)
                        }else{
                            id++
                        }
                    }
                }
                if(maxX < maxY && maxY > maxZ){
                    //la Y è la più usata, elimina tutti quelli che hanno una Y diversa
                    var id = 0
                    while(id < filt.size){
                        if(filt[id].first[1] != y){
                            filt.removeAt(id)
                        }else{
                            id++
                        }
                    }
                }
                if(maxX < maxZ && maxY < maxZ){
                    //la Z è la più usata, elimina tutti quelli che hanno una Z diversa
                    var id = 0
                    while(id < filt.size){
                        if(filt[id].first[2] != z){
                            filt.removeAt(id)
                        }else{
                            id++
                        }
                    }
                }
            }
        }
        return filt
    }

    private fun getNear(x : Long, y : Long, z : Long) : ArrayList<MineCube> {
        val pairNear = this.getNearPair(x,y,z)
        val filtered = ArrayList<MineCube>()
        for(curr in pairNear){
            if(curr.first[0] == x && curr.first[1] == y && curr.first[2] == z){
                continue
            }
            filtered.add(curr.second)
        }
        return filtered
    }

}