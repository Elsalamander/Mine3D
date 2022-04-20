package it.elsalamander.mine3d.ADT

import it.elsalamander.mine3d.ADT.exception.NotValidPointDimension


/*********************************************************************
 *
 * Classe per definire l'interfaccia pubblica del mio ADT.
 *
 * Per essere allocato mi serve avere un piano di partenza,
 * per poter iniziare ad allocare devo specificare i mondi su
 * cui la mia Zona agisce.
 * Usando la classe "ElementoArea" definisco il trio (World, X, Z)
 * per indicare dove si piazzerà nel mio ADT questo elemento d'area
 *
 * Insieme ci si può mettere un qualsiasi generico
 *
 * @author: Elsalamander
 * @data: 21 lug 2021
 * @version: v3.1
 */
class Zona<T>(private val piano: Piano<T>) {


    private val area: Area<T>

    /**
     * Costruttore, senza mondi e aree associate
     */
    init{
        area = Area(piano)
    }

    /**
     * Inserisci nella Zona l'elemento d'area designato e il valore
     * da memorizzare.
     * Ritorna il valore che ha sostituito se c'è n'era uno,
     * altrimenti NULL.
     * @param coords
     * @param value
     * @return
     * @throws NotValidPointDimension
     */
    @Throws(NotValidPointDimension::class)
    fun put(coords: Point, value: T): T? {
        if (!checkValdityPoint(coords)) {
            throw NotValidPointDimension(
                String.format(
                    ERR_MSG_INVALID_POINT_DIMENSION,
                    coords.getDimension(),
                    piano.getDimension()
                )
            )
        }
        return area.put(coords, value)
    }

    /**
     * Rimuove l'elemento d'area dato dalla Zona se esiste
     * Ritorna il valore che conteneva il nodo se c'era,
     * altrimenti NULL.
     * @param coords
     * @return
     * @throws NotValidPointDimension
     */
    @Throws(NotValidPointDimension::class)
    fun remove(coords: Point): T? {
        if (!checkValdityPoint(coords)) {
            throw NotValidPointDimension(
                String.format(
                    ERR_MSG_INVALID_POINT_DIMENSION,
                    coords.getDimension(),
                    piano.getDimension()
                )
            )
        }
        return area.remove(coords)
    }

    /**
     * Rimuovi tutti gli elementi di questa zona, in modo più efficente di fare
     * tanti remove singoli.
     */
    fun removeAll() {
        area.removeAll()
    }

    /**
     * Ritorna l'elemento T date le coordinate tramite l'elemento d'area
     * ma solo nella piccola parte di albero che contiene tutti gli elementi
     * di questa area, quindi se è fuori ritorna NULL anche se potrebbe avere un
     * valore definito.
     *
     * Da usare generalemente per fare il get di elementi dell'area.
     * @param coords
     * @return
     * @throws NotValidPointDimension
     */
    @Throws(NotValidPointDimension::class)
    fun getInArea(coords: Point): T? {
        if (!checkValdityPoint(coords)) {
            throw NotValidPointDimension(
                String.format(
                    ERR_MSG_INVALID_POINT_DIMENSION,
                    coords.getDimension(),
                    piano.getDimension()
                )
            )
        }
        return area[coords]
    }

    /**
     * Riotorna il valore in quel elemento d'area
     * partendo su tutto il piano
     *
     * Da usare generalemente per fare il get di elementi fuori dall'area.
     * @param coords
     * @return
     * @throws NotValidPointDimension
     */
    @Throws(NotValidPointDimension::class)
    operator fun get(coords: Point): T? {
        if (!checkValdityPoint(coords)) {
            throw NotValidPointDimension(
                String.format(
                    ERR_MSG_INVALID_POINT_DIMENSION,
                    coords.getDimension(),
                    piano.getDimension()
                )
            )
        }
        return piano[coords]
    }

    /**
     * Ritorna una lista di tutti i gli elementi d'area che compone
     * la zona dato il mondo
     */
    val list: List<T?>
        get() = area.elementiArea

    /**
     * Ritorna se questa zona ha l'elemento d'area passato al suo interno e non di altri o di nessuno.
     * @param coords
     * @return
     * @throws NotValidPointDimension
     */
    @Throws(NotValidPointDimension::class)
    fun contain(coords: Point): Boolean {
        if (!checkValdityPoint(coords)) {
            throw NotValidPointDimension(
                String.format(
                    ERR_MSG_INVALID_POINT_DIMENSION,
                    coords.getDimension(),
                    piano.getDimension()
                )
            )
        }
        val tmp = piano.getAreaNode(coords)
        return if (tmp != null) tmp == area else false
    }

    /**
     * Controlla che il punto è valido per essere inserito!
     * @param coords
     * @return
     */
    private fun checkValdityPoint(coords: Point): Boolean {
        return coords.getDimension().toInt() == this.piano.getDimension()
    }

    /**
     * Funziione di Visita delle foglie, skippa i nodi che non sono foglie per quanto riguarda
     * l'esecuzione dell lambda passata come paramentro
     * @param function
     */
    fun visitLeaf(function: (NodeSAH<Pair<Area<T>, T>?>?) -> Unit) {
        this.area.visitLeaf(function)
    }

    companion object {
        private const val ERR_MSG_INVALID_POINT_DIMENSION = ("Dimensione del punto non valida!\n"
                + "Dimensione del punto passato: %d\n"
                + "Dimensione richiesta: %d")
    }


}
