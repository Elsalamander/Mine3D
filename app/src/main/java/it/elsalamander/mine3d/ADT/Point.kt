package it.elsalamander.mine3d.ADT

import java.util.*

/******************************************************
 *
 * Creazione punto denerico di uno spazione qualsiasi,
 * di longeri.
 *
 * @author Elsalamander
 *******************************************************/
class Point(private val coords: LongArray) : Cloneable, Comparable<Point?> {

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + coords.contentHashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as Point
        return coords.contentEquals(other.coords)
    }

    override fun toString(): String {
        return "Point [coords=" + Arrays.toString(coords).toString() + "]"
    }

    /**
     * Ritorna quante dimensioni ha questo punto
     * @return
     */
    fun getDimension() : Long{
        return coords.size.toLong()
    }

    /**
     * Ritorna il valore dell'asse axis
     * @param axis
     * @return
     */
    fun getAxisValue(axis: Byte): Long {
        return coords[axis.toInt()]
    }

    /**
     * Setta il valore nell'asse
     * @param axis
     * @param value
     */
    fun setAxisValue(axis: Byte, value: Long) {
        coords[axis.toInt()] = value
    }

    /**
     * Ritorna un clone del vettore coords
     * @return
     */
    fun getCoords(): LongArray {
        return coords.clone()
    }

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Any {
        return Point(coords)
    }

    override fun compareTo(point: Point?): Int {
        var compare = 0
        for (i in coords.indices) {
            if (point != null) {
                if (coords[i] > point.getAxisValue(i.toByte())) {
                    compare += 1 shl i
                } else if (coords[i] < point.getAxisValue(i.toByte())) {
                    compare -= 1 shl i
                }
            }
        }
        return compare
    }

}