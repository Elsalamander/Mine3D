package it.elsalamander.mine3d.ADT

/*********************************************************************
 *
 * Oggetto per gestire i dati, un albero con nodi da 4 figli, in più specifico
 * un multiWaySearchTree fatto apposta per gli elementi d'area i quali i nodi
 * sono quadrati e i figli sono porzioni di essi utilizzando semplici
 * disuguaglianze e divisioni per 2.
 *
 * L'albero si ingrandisce o rimpicciolisce a seconda dei casi
 *
 *
 * @author: Elsalamander
 * @data: 17 lug 2021
 * @version: v3.1
 ***********************************************************************************/
class SAH<T>() {

    var root: NodeSAH<T?>? = null

    /**
     * Inserisci nell'abero un elemento d'area con l'argomento dato L'elemento
     * d'area è identificato dalle coordinare X e Z
     *
     * Il valore di ritorno è il valore Generico che aveva il nodo al posto di
     * quello nuovo.
     *
     * Da usare se non si ha un punto di partenza in particolare.
     *
     * @param value
     * @return
     */
    @Synchronized
    fun put(coords: Point?, value: T): T? {
        // controlla se esiste una radice
        if(this.root == null){
            // non ha radice, crea nodo e ponilo come radice
            this.root = NodeSAH(coords!!, 0.toByte(), null, value)
            return null
        }
        while(!this.root!!.contain(coords!!)){
            this.root = this.root!!.createPadre()
        }
        return putFrom(this.root, coords, value)
    }

    /**
     * Esegui il put a partire dal nodo dato, sapendo a priori che il nodo dato
     * contiene le coordinate da inserire, se è NULL il put parte della radice.
     *
     * @param node
     * @param coords
     * @return
     */
    @Synchronized
    fun putFrom(node: NodeSAH<T?>?, coords: Point?, value: T): T? {
        if (node == null) {
            return put(coords, value)
        }
        // se invece le coordinate date stanno sotto la radice trova la foglia
        // costruendo il percorso
        // ottieni la foglia, dalla radice se sta sotto
        val foglia = this.search(node, coords, true)

        // controllo se è una foglia
        if (foglia!!.isFoglia()) {
            // vecchio valore, e imposta quello nuovo
            val tmp: T? = node.getVal()
            foglia.setVal(value)
            return tmp
        }
        return null
    }

    /**
     * Rimuovi questa foglia e tutto il ramo che esisteva solo per lei
     *
     * @param coords
     * @return
     */
    @Synchronized
    fun removeNode(coords: Point?): T? {
        return removeNodeFrom(this.root, coords)
    }

    /**
     * Rimuovi il nodo, cercando a partire dal nodo dato
     *
     * @param node
     * @param coords
     * @return
     */
    @Synchronized
    fun removeNodeFrom(node: NodeSAH<T?>?, coords: Point?): T? {
        val foglia = this.search(node, coords)

        // controlla se esisteva una foglia in quelle coordinate
        if(foglia == null || !foglia.isFoglia()){
            // non esisteva, ritorna null
            return null
        }
        val tmp: T? = foglia.getVal()
        val nd = delete(foglia, true)
        if(nd != null){
            delete(nd, false)
        }
        return tmp
    }

    /**
     * Cerca la foglia, se c'è
     *
     * @param coords
     * @return
     */
    @Synchronized
    fun search(coords: Point?): NodeSAH<T?>? {
        return this.search(coords, false)
    }

    /**
     * Cerca la foglia, se c'è Se reate è TRUE crea il percorso e la foglia
     * restituendoola alla fine Il valore di ritorno è l'ultimo nodo diverso da NULL
     * se esiste una radice per andare verso le coordinate date
     *
     * @param coords
     * @param create
     * @return
     */
    @Synchronized
    protected fun search(coords: Point?, create: Boolean): NodeSAH<T?>? {
        return this.search(this.root, coords, create)
    }

    /**
     * Cerca la foglia con le coordinate date a partire dal nodo dato Questa
     * funzione server per migliorare funzioni future
     *
     * @param start
     * @param coords
     * @return
     */
    @Synchronized
    fun search(start: NodeSAH<T?>?, coords: Point?): NodeSAH<T?>? {
        return this.search(start, coords, false)
    }

    /**
     * Cerca la foglia con le coordinate date a partire dal nodo dato Questa
     * funzione server per migliorare funzioni future
     *
     * @param start
     * @param coords
     * @param create
     * @return
     */
    @Synchronized
    private fun search(start: NodeSAH<T?>?, coords: Point?, create: Boolean): NodeSAH<T?>? {
        var start = start
        if(start == null || !start.contain(coords!!)){
            return null
        }
        var node: NodeSAH<T?>? = start
        while(start != null){
            node = start
            start = start.getFiglio(coords, create)
        }

        if (node != null) {
            if(!node.isFoglia()){
                return null
            }
        }

        // ho la foglia desiderata?
        if (node != null) {
            return if(node.getPoint() == coords){
                node
            }else null
        }
        return null
    }

    /**
     * Elimina i nodi in modo ricorsivo dir = TRUE : Elimina salendo dir = FALSE:
     * Elimina scendendo
     *
     * @param node
     * @param dir
     * @return
     */
    @Synchronized
    private fun delete(node: NodeSAH<T?>?, dir: Boolean): NodeSAH<T?>? {
        return if (dir) {
            // elimina salendo
            // prendi il padre, e rimuovi il figlio node
            val padre: NodeSAH<T?> = node?.getPadre() ?: return node

            // controlla il padre

            // cerca il figlio
            var count: Byte = 0
            for(i in 0 until (padre.getFigli()?.size ?: 0)){
                val tmp = padre.getFiglio(i)
                if(tmp != null){
                    if(tmp == node){
                        // elimina il figlio
                        padre.setFiglio(i, null)
                    }else{
                        count++
                    }
                }
            }

            // eliminato il figlio designato, controlla quanti figli validi ha il padre
            // tramite la variabile count, se è 0 non ne ha
            if(count.toInt() == 0){
                delete(padre, true)
            }else{
                // finisci la ricorsione
                padre
            }
        }else{
            // elimina scendendo
            // controlla se è una foglia
            if(node!!.isFoglia()){
                // è una foglia, non deve essere eliminata
                return null
            }

            // per decidere di eliminare il nodo scendendo prima se devono controllare
            // quanti figli ha
            var count: Byte = 0
            var index: Byte = -1
            for(i in 0 until (node.getFigli()?.size ?: 0)){
                if (node.getFiglio(i) != null) {
                    count++
                    index = i.toByte()
                }
            }

            // il nodo si deve eliminare se è solo se count == 1
            if(count.toInt() == 1){
                // prendi il nodo valido tramite index e esegui la ricorsione
                // elimina nel figlio valido il padre
                node.getFiglio(index.toInt())?.setPadre(null)
                delete(node.getFiglio(index.toInt()), false)
            }else{
                null
            }
        }
    }

    /**
     * Calcola in modo efficente l'altezza del nodo in cui si diramano i 2 rami
     * dove uno porta a "from" e l'altro  "to", se l'altezza è (AltezzaRadice/2 > altezza trovata)
     * usa il percorso più veloce, altrimenti parte dalla radice.
     * @param from
     * @param to
     * @return
     */
    fun getFrom(from: NodeSAH<T?>, to: Point): NodeSAH<T?>? {
        //prendo quello che risulta più profondo

        //per ogni asse
        val n: Long = from.getPoint().getDimension()
        var goUp = 0 //valore più alto trovato
        var tmp_a: Int //valore temporaneo per il calcolo
        var tmp_b: Int //valore temporaneo per il calcolo
        var tmp_c: Int //valore temporaneo per il calcolo
        for(i in 0 until n){
            tmp_a = (from.getPoint().getAxisValue(i.toByte()) - 1).toInt()
            tmp_b = to.getAxisValue(i.toByte()).toInt() - 1
            tmp_c = 32 - Integer.numberOfLeadingZeros(tmp_a xor tmp_b)
            goUp = if (goUp < tmp_c) tmp_c else goUp
        }
        var start: NodeSAH<T?>?
        //confronto i con l'altezza max
        if((this.root!!.getLogSize()) / 2 > goUp){
            //ho un guadagno
            start = from
            for(i in 0 until goUp){
                if (start != null) {
                    start = start.getPadre()
                }
            }
        }else{
            start = this.root
        }
        var tmp = start
        while(tmp != null){
            start = tmp
            tmp = start.getFiglio(to, false)
        }
        return start
    }

    companion object {
        /**
         * Cerca il nodo che racchiude tutte le foglie con val dato
         *
         * @param nodeSAH
         * @param area
         * @return
         * @return
         */
        @Synchronized
        fun <T> getRootSubTree(nodeSAH: NodeSAH<Pair<Area<T>, T>?>?, area: Area<T>): NodeSAH<Pair<Area<T>, T>?>? {
            searchClearSupp(nodeSAH)
            searchMapSubTree(nodeSAH, area)
            return getHigherSupp(nodeSAH, 2.toByte(), 1.toByte())
        }

        /**
         * Ritorna il nodo piu in alto che ha come variabile di support maggiore o
         * uguale di val
         *
         * @param nodo
         * @param value
         * @return
         */
        @Synchronized
        private fun <T> getHigherSupp(nodo: NodeSAH<Pair<Area<T>, T>?>?, value: Byte, valf: Byte): NodeSAH<Pair<Area<T>, T>?>? {
            if (nodo == null) {
                return null
            }
            if (nodo.isFoglia()) {
                return if (nodo.getSupp() >= valf) {
                    nodo
                } else null
            }

            // non è una foglia
            // lui ha un valore maggiore o uguale?
            if (nodo.getSupp() >= value) {
                return nodo
            }

            // chiamata ricorsiva sui figli
            for (i in 0 until (nodo.getFigli()?.size ?: 0)) {
                val tmp: NodeSAH<Pair<Area<T>, T>?>? = nodo.getFiglio(i)
                if (tmp != null) {
                    val nd: NodeSAH<Pair<Area<T>, T>?>? = getHigherSupp(tmp, value, valf)
                    if (nd != null) {
                        return nd
                    }
                }
            }
            return null
        }

        /**
         * @param nodo
         * @return
         */
        @Synchronized
        private fun <T> searchMapSubTree(nodo: NodeSAH<Pair<Area<T>, T>?>?, area: Area<T>) {
            if (nodo == null) {
                return
            }
            if (nodo.isFoglia()) {
                if (nodo.getVal()?.first?.equals(area) == true) {
                    nodo.setSupp(1.toByte())
                } else {
                    nodo.setSupp(0.toByte())
                }
                return
            }

            // non è una foglia
            // chiamata ricorsiva sui figli
            var check: Byte = 0
            for (i in 0 until (nodo.getFigli()?.size ?: 0)) {
                val tmp: NodeSAH<Pair<Area<T>, T>?>? = nodo.getFigli()?.get(i)
                if (tmp != null) {
                    searchMapSubTree(tmp, area)
                    if (tmp.getSupp().toInt() != 0) {
                        check++
                    }
                }
            }
            // imposta il valore di supporto
            nodo.setSupp(check)
        }

        /**
         * Azzera tutti i valori di supporto
         *
         * @param node
         */
        @Synchronized
        private fun <T> searchClearSupp(node: NodeSAH<Pair<Area<T>, T>?>?) {
            if (node == null) {
                return
            }
            if (node.isFoglia()) {
                node.setSupp(0.toByte())
                return
            }

            // non è una foglia
            // chiamata ricorsiva sui figli
            node.setSupp(0.toByte())
            for (i in 0 until (node.getFigli()?.size ?: 0)) {
                val tmp: NodeSAH<Pair<Area<T>, T>?>? = node.getFigli()?.get(i)
                if (tmp != null) {
                    searchClearSupp(tmp)
                }
            }
        }
    }
}
