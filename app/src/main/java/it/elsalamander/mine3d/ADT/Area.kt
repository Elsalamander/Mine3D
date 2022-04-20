package it.elsalamander.mine3d.ADT



/*********************************************************************
 *
 * Classe per definire l'area di un mondo specificato.
 *
 * Per migliorare il get nell'area data ci sono 4 nodi di cache
 * uno per albero del piano, questi nodi sono tali che raggruppano
 * tutti i nodi appartenenti a questa AREA, la sua posizione e anche
 * tale che i suoi figli che portano ad una foglia di questa area sono
 * almeno 2, tranne nel caso che questi nodi non coincidono con la foglia
 *
 * L'aggiornamento della cache è automatico.
 *
 * @author: Elsalamander
 * @data: 19 lug 2021
 * @version: v3.0
 **************************************************************************/
class Area<T>(private val piano: Piano<T>) {

    private val cache: Array<NodeSAH<Pair<Area<T>, T>?>?> = arrayOfNulls<NodeSAH<Pair<Area<T>, T>?>?>(1 shl piano.getDimension())

    /**
     * Inserisci nel piano il valore dato alle coordinate date
     * Complessità totale è pari a O(log(n)) se non sostituisce un elelemto
     * già esistente, altrimenti la complesiià è O(n) a causa dell'aggiornamento
     * della cache da parte dell'area in cui è stato sottratto un elemento.
     * @param coords
     * @param value
     * @return
     */
    fun put(coords: Point, value: T): T? {
        val tmp: Pair<Area<T>, T>? = if (contain(coords)) {
            //è sotto uno dei miei nodi di cache!
            piano.putFrom(getCacheSemiPiano(coords), coords, Pair(this, value))
        } else {
            //non è sotto a un nodo di cache.
            piano.put(coords, Pair(this, value))
        }

        //se non è nulla devo aggiornare i nuovi valori di cache degli altre aree
        if (tmp != null) {
            //è un nodo della stessa area?
            //se si significa che la cache rimante la stessa
            return if (tmp.first.equals(this)) {
                //la cache rimante ferma dov'è ora

                //ritorna l'oggetto che contiene
                tmp.second
            } else {
                //aggiorna la cache dell'area modificata
                piano.getSemiPiano(coords)?.let { tmp.first.upDateCache(it, coords) }

                //aggiorna la cache della mia area appena modificata
                upDateCacheOptimazedPut(coords)

                //ritorna l'oggetto che contiene
                tmp.second
            }
        }
        //non ho rimpiazzato nodi, aggiorna la cache.
        upDateCacheOptimazedPut(coords)

        //non avendo rimpiazzato ritorna NULL
        return null
    }

    /**
     * Rimuovi l'emento alle coordinate date
     * Complessità O(n) a causa dell'aggiornamento della
     * cache dell'area.
     * @param coords
     * @return
     */
    fun remove(coords: Point): T? {
        val tmp: Pair<Area<T>?, T>?

        //controlla di che area fa parte
        val area: Area<T>? = piano.getAreaNode(coords)

        //è la stessa area?
        if (this != area) {
            //non è la stessa, NON rimuovere e ritorna NULL
            return null
        }

        //verifica che il nodo passato è contenuto nell'area.
        if (contain(coords)) {
            //se si, come dovrebbe essere :), esegui il remove partendo dal nodo di cache
            tmp = piano.remove(getCacheSemiPiano(coords), coords)
        } else {
            //non dovrebbe accadere :(, esegui il remov dalla radice
            tmp = piano.remove(coords)
        }

        //controlla se il nodo eliminato è diverso da NULL come dovrebbe essere
        if (tmp != null) {
            //aggiorna la cache dell'area
            piano.getSemiPiano(coords)?.let { upDateCache(it, coords) }
            return tmp.second
        }
        return null
    }

    /**
     * Controlla se le coordinate date hanno un valore assegnato,
     * usato come supporto la funzione di GET(x,z)
     * Complessità O(log(N))
     * @param coords
     * @return
     */
    fun contain(coords: Point): Boolean {
        return if (getCacheSemiPiano(coords) != null) getCacheSemiPiano(coords)!!.contain(coords) else false
    }

    /**
     * Ritorna il valore assegnato alle coordiante date
     * Se esiste ritorna il valore nelle coordinate altrimenti
     * ritorna NULL.
     * Complessità totale O(log(N))
     * @param coords
     * @return
     */
    operator fun get(coords: Point): T? {
        val semi: SAH<Pair<Area<T>, T>>? = piano.getSemiPiano(coords)
        val tmp = semi?.search(getCacheSemiPiano(coords), coords)
        return if (tmp != null) {
            if (tmp.getVal() != null) tmp.getVal()?.second else null
        } else null
    }

    /**
     * Aggiorna la cache in riferimento al semipiano.
     * Complessità: O(N)
     * @param semi
     * @param coords
     */
    private fun upDateCache(semi: SAH<Pair<Area<T>, T>>, coords: Point) {
        setCacheSemiPiano(coords, SAH.getRootSubTree(semi.root, this))
    }

    /**
     * Complessità di aggiornamento della cache è pari a O(log(n)) rispetto la funzione di
     * upDateCache normale di O(n)
     * @param coords
     */
    private fun upDateCacheOptimazedPut(coords: Point) {
        //controllo se c'è il nodo di cache
        val semi: SAH<Pair<Area<T>, T>>? = piano.getSemiPiano(coords)
        val cache = getCacheSemiPiano(coords)
        //se non c'è significa che quello inserito è il nuovo nodo di cache
        if (cache == null) {
            setCacheSemiPiano(coords, semi?.search(coords))
            return
        }

        //se lo contiene di già non serve risettarlo.
        if (cache.contain(coords)) {
            return
        }


        //non è il primo nodo
        //controlla se il padre contiene le coordinate nuove, se non le contiene bisogna salire
        //se le contiene siamo apposto qua
        var nd = cache
        while (!nd!!.contain(coords)) {
            nd = nd.getPadre()
        }

        //node ora raffigura il nodo che comprende non solo i precedenti ma anche quello nuovo
        //settalo come cache
        setCacheSemiPiano(coords, nd)
    }

    /**
     * Ritorna la cache dell'area con le coordinate date
     * @param coords
     * @return
     */
    fun getCacheSemiPiano(coords: Point): NodeSAH<Pair<Area<T>, T>?>? {
        var index = 0 //parto da 1

        //trova l'elemento interno e crea le coordinate per l'eventuale figlio
        for (i in 0 until coords.getDimension()) {
            if (coords.getAxisValue(i.toByte()) > 0) {
                index = index or (1 shl i.toInt())
            }
        }
        return cache[index]
    }

    /**
     * Imposta il valore della cache in base alle coordinate date
     * @param coords
     * @param node
     * @return
     */
    private fun setCacheSemiPiano(coords: Point, node: NodeSAH<Pair<Area<T>, T>?>?) {
        var index = 0 //parto da 1

        //trova l'elemento interno e crea le coordinate per l'eventuale figlio
        for (i in 0 until coords.getDimension()) {
            if (coords.getAxisValue(i.toByte()) > 0) {
                index = index or (1 shl i.toInt())
            }
        }
        cache[index] = node
    }

    /**
     * Ritorna una lista di elementi d'area dell'AREA in questione.
     */
    val elementiArea: List<T?>
        get() {
            val tmp = elementList
            val list: MutableList<T> = ArrayList()
            for (node in tmp) {
                node.getVal()?.second?.let { list.add(it) }
            }
            return list
        }

    //crea la lista
    /**
     * Riotrna la lista dei nodi con il valore Val dato
     * @return
     */
    private val elementList: List<NodeSAH<Pair<Area<T>, T>?>>
        get() {
            //lista dei nodi
            val list: MutableList<NodeSAH<Pair<Area<T>, T>?>> = ArrayList()

            //crea la lista
            for (tmp in cache) {
                createList(tmp, list)
            }
            return list
        }

    /**
     * Funzione ricorsiva per per la stesura della lista
     * @param node
     * @param list
     */
    private fun createList(
        node: NodeSAH<Pair<Area<T>, T>?>?,
        list: MutableList<NodeSAH<Pair<Area<T>, T>?>>
    ) {
        //controllo di base
        if (node == null) {
            return
        }

        //controlla se è una foglia, di questa area
        if (node.isFoglia()) {
            //è una foglia, controlla se fa parte della stessa area.
            if (node.getVal()?.first?.equals(this) == true) {
                //aggiungi alla lista
                list.add(node)
            }
            //essendo foglia posso terminare la funzione con un return.
            return
        }

        //chiama la ricorsione sui figli
        for (i in 0 until (node.getFigli()?.size ?: 0)) {
            createList(node.getFigli()!![i], list)
        }
    }

    /**
     * Funzione ottimizzata per rimuovere tutti gli elementi d'area sotto a questa
     * Area
     *
     * Per rimuovere tutto eseguendo più volte il remove si avrebbe una complessita
     * quadratica, invece questaa funzione ha una complessità al più O(N), migliorando
     * le prestazioni.
     */
    fun removeAll() {
        //elimna tutti i nodi legati al valore passato
        //elimina tutti i i nodi di cache
        for (i in cache.indices) {
            val tmp = cache[i]
            deleteAllStart(tmp)
            cache[i] = null
        }
    }

    /**
     * Elimina tutti i nodi di questa area e i nodi che diventano
     * se lo diventano senza figli
     * @param node
     */
    private fun deleteAllStart(node: NodeSAH<Pair<Area<T>, T>?>?) {
        //la funzione deleteAllSub elimina tutti i nodi sotto stanti
        //al nodo di cache uquali a VAL però non è detto che
        //il nodo di cache debba rimanere, se non ha altri figli questo
        //deve essere eliminato

        //procedo con l'eliminazione dei valori sottostanti
        //se la funzione ritorna TRUE devo riccorsivamente eliminare
        //anche i nodi a partire da quello della cache finche non
        //trovo un nodo con almen 2 figli, la quale uno è quello da eliminare

        //controllo se c'è qualcosa da eliminare
        if (node == null) {
            //non c'è nulla da eliminare
            return
        }
        if (deleteAllSub(node)) {
            deleteAllUp(node)
        }
    }

    /**
     * Elina tutti i nodi che a causa della eliminazione dell'area
     * rimangono senza figli
     * @param node
     */
    private fun deleteAllUp(node: NodeSAH<Pair<Area<T>, T>?>?) {
        //devo eliminare questo nodo, ovvero devo eliminarlo
        //come figlio del padre

        //casi base
        if (node == null) {
            //non devo eliminare più niente
            return
        }

        //ottieni il padre
        val padre = node.getPadre()
        if (padre == null) {
            if (node.isFoglia()) {
                val semi: SAH<Pair<Area<T>, T>>? = piano.getSemiPiano(node.getPoint())
                semi?.root = null
                //termina
                return
            }
            //node è la radice del semipiano, WOW
            //node in quanto radice che deve essere eliminata
            //se evo eliminare la radice devo impostare un nuova radice
            //controllo quanti figli ha
            var count = 0
            var index = -1
            for (i in 0 until (node.getFigli()?.size ?: 0)) {
                if (node.getFiglio(i) != null) {
                    count++
                }
            }
            //controllo quanti figli ha
            val semi: SAH<Pair<Area<T>, T>>? = piano.getSemiPiano(node.getPoint())
            if (count == 0) {
                //non ha figli, elimino la radice
                semi?.root = null
            } else if (count == 1) {
                //ha un figlio la radice si sposta
                var ripeti = true
                while (ripeti) {
                    count = 0
                    if (semi != null) {
                        for (i in 0 until (semi.root?.getFigli()?.size ?: 0)) {
                            if (semi.root!!.getFiglio(i) != null) {
                                count++
                                index = i
                            }
                        }
                    }
                    //controllo count
                    if (count == 1) {
                        //elimina sta radice e rimpiazza
                        semi?.root = semi?.root!!.getFiglio(index)
                    } else {
                        ripeti = false
                    }
                }
                return
            } else {
                //non eliminare ha piu di 2 figli
                return
            }
            return
        }

        //conta quanti figli validi ha
        var count = 0
        for (i in 0 until (padre.getFigli()?.size ?: 0)) {
            val n = padre.getFigli()!![i]
            if (n == null) {
                //non è valido
                count++
            } else {
                //valido
                //controllo se è il figlio da eliminare
                if (n == node) {
                    //è il figlio da eliminare
                    count++
                    //elimina come figlio
                    padre.setFiglio(i, null)
                }
            }
        }

        //controllo il valore di count
        if (count == padre.getFigli()?.size ?: 0) {
            //chiamata ricorsiva per eliminare questo nodo
            deleteAllUp(padre)
        }
        //finisci la ricorsione
        return
    }

    /**
     * Elimina tutti i nodi di questa area
     * @param node
     * @return
     */
    private fun deleteAllSub(node: NodeSAH<Pair<Area<T>, T>?>?): Boolean {
        //devo scendere dal nodo dato
        //quando trovo una foglia elimino tutti i valori uguali a VAL
        //se elimino tutti i valori(considero anche quelli che erano null come eliminati)
        //quindi il nodo non ha figli, devo eliminare anche il nodo stesso, per farlo
        //la funzione ritorna true, se deve eliminare anche il nodo chiamante come figlio

        //controllo se il nodo passato è null
        if (node == null) {
            //non serve eliminare tanto già non esiste
            return false
        }

        //controllo se è foglia
        if (node.isFoglia()) {
            //il valore che ha è quello dato?
            return this == node.getVal()?.first
        }

        //se non è foglia chiamata ricorsiva sui figli, e controlla il risultato
        //Count conta quanti nodi sono NULL e quanti ne ho eliminati
        //se count è ==4 significa che non ha più figli e devo eliminare anche questo nodo
        var count = 0
        for (i in 0 until (node.getFigli()?.size ?: 0)) {
            val tmp = node.getFiglio(i)
            //controlla se è un figlio valido
            if (tmp == null) {
                count++
            } else {
                val del = deleteAllSub(tmp)
                //devo eliminare il nodo?
                if (del) {
                    //elimina il nodo
                    node.setFiglio(i, null)
                    count++
                }
            }
        }

        //controllo il valore di count
        return count >= node.getFigli()?.size ?: 0

        //il nodo non deve essere eliminato
    }

    /**
     * Visita tutte le foglie dei piani
     */
    fun visitLeaf(function: (NodeSAH<Pair<Area<T>, T>?>?) -> Unit) {
        //per ogni radice dei P piani
        for(p in this.cache){
            //parto dalla radice, prendi il primo figlio valido a SX finchè non trovo una foglia
            this.visitLeaf(p, function)
        }
    }

    private fun visitLeaf(node : NodeSAH<Pair<Area<T>, T>?>?, function: (NodeSAH<Pair<Area<T>, T>?>?) -> Unit){
        //caso base, se è null
        if(node == null){
            return
        }

        //caso base, controllo se è una foglia
        if(node.isFoglia()){
            //è una foglia esegui la lambda
            function.invoke(node)
            return
        }

        //non è una foglia ma un nodo interno.
        for(f in node.getFigli()!!){
            if(f != null){
                this.visitLeaf(f, function)
            }
        }
    }

}
