package it.elsalamander.mine3d.ADT

import it.elsalamander.mine3d.ADT.exception.NotValidPointDimension
import it.elsalamander.mine3d.util.Pair

/*********************************************************************
 *
 * Oggetto per organizzare, memorizzare e associare un elemento d'area
 * con un oggetto tramite alberi con una certa efficenza.
 *
 * Per iniziare il piano (cartesiano) viene diviso in 4, nei rispettivi
 * quadranti lo 0 fa parte dei negativi.
 *
 * Dunque si hanno 4 SAH<T> ogni area poi avrà un puntatore per ogni SAH
 * che raffigura il Nodo più in alto che contiene l'oggetto dato come
 * cache, in modo da non andare a cercare su tutti i 4 alberi ma solo
 * in alcune porzioni di alberi, essendo 4 alberi, la ricerca di un
 * elemento(non sapendo le coordinate) può essere parallela, la funzione
 * chiamante cerca su un albero, si generano altri 3 task async che
 * cercano nei rispettivi 3 alberi rimaneti, tutti i task devono essere
 * conclusi per avere effettivamente la conclusione della ricerca.
 *
 * Ma questa cosa complicata non mi serve
 *
 * @author: Elsalamander
 * @data: 19 lug 2021
 * @version: v2.0
 ******************************************************************************/
class Piano<T>(dimension: Byte) {

    private val piani: Array<SAH<Pair<Area<T>, T>>?> = arrayOfNulls<SAH<Pair<Area<T>, T>>>(1 shl dimension.toInt())
    private val dimension: Byte
    private var noZona : Zona<T>


    init{
        for (i in 0 until (1 shl dimension.toInt())) {
            piani[i] = SAH<Pair<Area<T>, T>>()
        }
        this.dimension = dimension
        noZona = Zona(this)
    }

    /**
     * Inserisci nel piano il valore dato alle coordinate date
     * @param coords
     * @param value
     * @return
     */
    fun put(coords: Point, value: Pair<Area<T>, T>): Pair<Area<T>, T>? {
        val semi = getSemiPiano(coords)
        return semi?.put(coords, value)
    }

    /**
     * Inserisci nel piano il valore dato alle coordinate date, partendo da
     * un nodo dato.
     * @param node
     * @param coords
     * @param value
     * @return
     */
    fun putFrom(node: NodeSAH<Pair<Area<T>, T>?>?, coords: Point, value: Pair<Area<T>, T>): Pair<Area<T>, T>? {
        val semi = getSemiPiano(coords)
        return semi?.putFrom(node, coords, value)
    }

    /**
     * Rimuovi l'emento alle coordinate date
     * @param coords
     * @return
     */
    fun remove(coords: Point): Pair<Area<T>, T>? {
        val semi = getSemiPiano(coords)
        return semi?.removeNode(coords)
    }

    /**
     * Rimuovi l'emento alle coordinate date
     * @param coords
     * @return
     */
    fun remove(node: NodeSAH<Pair<Area<T>, T>?>?, coords: Point): Pair<Area<T>, T>? {
        val semi = getSemiPiano(coords)
        return semi?.removeNodeFrom(node, coords)
    }

    /**
     * Controlla se le coordinate date hanno un valore assegnato
     * @param coords
     * @return
     */
    fun contain(coords: Point): Boolean {
        return this[coords] != null
    }

    /**
     * Ritorna il valore assegnato alle coordiante date
     * @param coords
     * @return
     */
    operator fun get(coords: Point): T? {
        val semi = getSemiPiano(coords)
        val node = semi?.search(coords)
        if (node != null) {
            val tmp = node.getVal()
            if (tmp != null) {
                return tmp.second
            }
        }
        return null
    }

    /**
     * Ritorna il semipiano date le coordinate
     * @param coords
     * @return
     */
    fun getSemiPiano(coords: Point): SAH<Pair<Area<T>, T>>? {
        var index = 0 //parto da 0

        //trova l'elemento interno e crea le coordinate per l'eventuale figlio
        for (i in 0 until coords.getDimension()) {
            if (coords.getAxisValue(i.toByte()) > 0) {
                index = index or (1 shl i.toInt())
            }
        }
        return piani[index]
    }

    /**
     * Cerca il nodo alle coordinate date
     * e ritorna l'oggetto AREA
     * @param coords
     * @return
     */
    fun getAreaNode(coords: Point): Area<T>? {
        val semi = getSemiPiano(coords)
        val node = semi?.search(coords)
        if (node != null) {
            val tmp = node.getVal()
            if (tmp != null) {
                return tmp.first
            }
        }
        return null
    }

    /**
     * @return
     */
    fun getDimension(): Int {
        return dimension.toInt()
    }

    /**
     * Crea una zona a partire dal piano
     * @return
     */
    fun createZona(): Zona<T> {
        return Zona(this)
    }

    /**
     * Aggiungi al piano un valore attribuito ad una zona fittizia
     * @param coords
     * @param value
     * @return
     * @throws NotValidPointDimension
     */
    @Throws(NotValidPointDimension::class)
    fun add(coords: Point, value: T): T? {
        return noZona.put(coords, value)
    }

    /**
     * Elimina dal piano un valore nella zona fittizia
     * @param coords
     * @return
     * @throws NotValidPointDimension
     */
    @Throws(NotValidPointDimension::class)
    fun delete(coords: Point): T? {
        return noZona.remove(coords)
    }


    /**
     * @param i
     * @return
     */
    fun getRoot(i: Int): NodeSAH<Pair<Area<T>, T>?>? {
        return piani[i]?.root
    }

}