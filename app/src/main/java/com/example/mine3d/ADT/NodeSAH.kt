package com.example.mine3d.ADT

import kotlin.experimental.and
import kotlin.experimental.or

/*********************************************************************
 *
 * Nodo per l'abero per il SAH.
 *
 * Esempio per point a 2 dimensioni:
 * Partizione i 4 dell'area:
 *
 * +-------+-------+
 * |   2   |   3   |
 * +-------+-------+
 * |   0   |   1   |
 * +-------+-------+
 * la croce in mezzo indica la coppia di coordinate (x z) del nodo,
 * gli assi interni.
 *
 * Le uguagliaze sono banali dopo aver tolto il rifermento delle coordiante del centro
 * dopo la sottrazione non si avranno mai valori non interi, in quanto
 * i lati di questi quadrati sono fra blocco e blocco e non un blocco in se
 *
 * @author: Elsalamander
 * @data: 17 lug 2021
 * @version: v4.2
 * @param <T>
 *****************************************************************************************/
class NodeSAH<T>(private var punto: Point, size: Byte, padre: NodeSAH<T>?, value: T?) {

    private var size  : Byte        //Grandezza nodo
    private var padre : NodeSAH<T>?  //padre del nodo, se è NULL è radice
    private var value : T? = null   //Valore immaganizzato
    private var figli: Array<NodeSAH<T>?>?   //figli presenti

    init{
        this.padre = padre
        this.size = (size and 0x3F)

        figli = if(this.size.toInt() == 0){
            //non allocare l'array per i figli
            null
        }else{
            arrayOfNulls<NodeSAH<T>>(1 shl punto.getDimension().toInt())
        }
        this.value = value
    }

    private constructor(punto: Point, size: Byte, value: T) : this(punto, size, null, value) {}

    private constructor(punto: Point, size: Byte, padre: NodeSAH<T>?) : this(punto,size, padre, null){}

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + punto.hashCode()
        result = prime * result + size
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val otherObj = other as NodeSAH<*>
        if (punto != otherObj.punto) return false
        return size == otherObj.size
    }

    /**
     * Imposta il figlio alla posizione Index, 0 - 3, ci sono solo 4 figli
     * ritorna il figlio sostituito se non c'era ritorna null
     * @param index
     * @param node
     * @return
     */
    fun setFiglio(index: Int, node: NodeSAH<T>?): NodeSAH<T>? {
        if (index < 1 shl punto.getDimension().toInt()) {
            val tmp = this.getFiglio(index)
            figli!![index] = node
            return tmp
        }
        return null
    }

    /**
     * Ritorna il figlio in posizione index, 0 - n, se non c'è ritorna null
     * @param index
     * @return
     */
    fun getFiglio(index: Int): NodeSAH<T>? {
        return if(figli == null){
            null
        }else figli!![index]
    }

    /**
     * Lunghezza lato di questo livello
     * @return the size
     */
    fun getSize(): Int {
        return 1 shl ((size and 0x3F).toInt())
    }

    /**
     * Ritorna l'esponente della base di 2 per ottenere il
     * lato del quadrato
     * @return
     */
    fun getLogSize(): Byte{
        return size and 0x3F
    }


    /**
     * Ottieni il figlio sapendo la coppia di coordinate, della foglia.
     * Lo shift sui numeri negativi non influisce sul segno.
     * Se "create" è TRUE crea eventualemente il nodo/figlio per avvicinarsi/raggiungere
     * le coordinate date.
     * @param to
     * @param create
     * @return
     */
    fun getFiglio(to: Point, create: Boolean): NodeSAH<T>? {
        //controlla la lunghezza per ccapire se posso scendere
        if (!contain(to)) {
            return null
        }
        if (isFoglia()) {
            return null
        }

        //ottieni linice di potenza del lato
        val sz = size and 0x3F

        //ottieni la dimensione del punto, "il numeri di assi"
        val axis = punto.getDimension().toByte()

        //index
        var index = 0 //parto da 0

        //array relativi per la creazione dell'eventuale figlio.
        var newPoint: LongArray? = null
        if(create){
            //alloco i punti temporanei per generare il figlio
            newPoint = LongArray(axis.toInt()) //di default sono tutti a zero.
        }

        //trova l'elemento interno e crea le coordinate per l'eventuale figlio
        for(i in 0 until axis){
            if(to.getAxisValue(i.toByte()) - (punto.getAxisValue(i.toByte()) - 1 shl sz.toInt()) - (1 shl sz - 1) > 0) {
                //solo se è maggiore
                index = index or (1 shl i)
                if(create){
                    newPoint!![i] = punto.getAxisValue(i.toByte()) shl 1
                }
            }else{
                if(create){
                    newPoint!![i] = (punto.getAxisValue(i.toByte()) shl 1) - 1
                }
            }
        }

        //prendi il figlio
        val tmp = figli!![index]

        //controllo se esiste il figlio e lo creao in caso
        if(tmp == null && create){
            //creo il figlio
            figli!![index] = NodeSAH(Point(newPoint!!), ((size and 0x3F) - 1).toByte(), this)

            //ritorna il figlio
            return figli!![index]
        }
        return tmp
    }

    /**
     * Controlla se l'elemento d'area sta dento a questo SAH.
     * Utilizzando come dati il Size del nodo è possibili tramite
     * una serie fissa di comparazioni, Complessità O(1)
     * @param foglia
     * @return
     */
    fun contain(foglia: Point): Boolean{
        //get info per lo size
        val siz = getSize()
        val sz: Int = (size and 0x3F).toInt()

        //controllo se è gia una foglia
        if(punto == foglia && sz == 0){
            return true
        }

        //ripetizione per ogni asse
        val axis = punto.getDimension().toByte()
        for(i in 0 until axis){
            //quanto e distante dal punto relativo
            val tmp =
                foglia.getAxisValue(i.toByte()) - (punto.getAxisValue(i.toByte()) - 1 shl sz) - (1 shl sz - 1)
            if(tmp > siz shr 1){
                return false
            }
            if(-tmp > (siz shr 1) - 1){
                return false
            }
        }
        return true
    }

    /**
     * Crea il padre che sta sopra a questo nodo
     * @return
     */
    fun createPadre(): NodeSAH<T>? {
        //crea un array lungo quanto la dimensione del punto
        val axis = punto.getDimension().toByte()
        val newPoint = LongArray(axis.toInt())

        //per ogni asse calcola il valore del nuovo punto e inseriscilo nell'array
        //cerca nel mentre anche la posisizone del figlio che deve avere nel padre
        var index: Byte = 0 //posizione del figlio ne padre
        for(i in 0 until axis){
            val tmp = punto.getAxisValue(i.toByte())

            //calcolo coordinata del padre
            newPoint[i] = (tmp + if (tmp <= 0) 0 else 1) / 2 //non posso mettere al posto di "/2" ">> 1" da errore

            //calcolo posizioe figlio
            if (tmp - (newPoint[i] shl 1) >= 0) {
                index = index or ((1 shl i).toByte())
            }
        }

        //nuovo size per il padre
        val newsize = ((size and 0x3F) + 1).toByte()

        //crea padre
        padre = NodeSAH<T>(Point(newPoint), newsize, null, null)

        //imposta il figlio nel padre appena creato
        padre!!.setFiglio(index.toInt(), this)

        //ritorna il padre
        return padre
    }

    override fun toString(): String {
        var tmp = "NodeSAH ["
        for (i in 0 until punto.getDimension()) {
            tmp += punto.getAxisValue(i.toByte()).toString() + "@"
        }
        return "$tmp$size]"
    }

    /**
     * Ritorna se è una radice
     * @return
     */
    fun isRoot() : Boolean{
        return this.padre == null
    }

    /**
     * Ritor
     * @return
     */
    fun isFoglia(): Boolean{
        return (size and 0x3F).toInt() == 0
    }

    /**
     * @return the supp
     */
    fun getSupp() : Byte{
        return ((size and 0xC0.toByte()).toInt() shr 6).toByte()
    }

    /**
     * @param supp the supp to set
     */
    fun setSupp(supp: Byte) {
        var tmp = supp
        tmp = if (tmp >= 4) 3 else tmp
        size = size and 0x3F
        size = size or (tmp.toInt() shl 6).toByte()
    }

    fun getVal(): T? {
        return this.value
    }

    fun setVal(valu : T?){
        this.value = valu
    }

    fun getPoint(): Point {
        return this.punto
    }

    fun getPadre(): NodeSAH<T>? {
        return this.padre
    }

    fun getFigli(): Array<NodeSAH<T>?>? {
        return this.figli
    }

    fun setPadre(t: NodeSAH<T>?) {
        this.padre = t
    }

}
