package it.elsalamander.mine3d.Game.Event.Manager

import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmErasure

/****************************************************************
 * Oggetto per la gestione degli eventi.
 * Gestire la chiamata ad evento degli oggetti che estendono la classe
 * "EventGame", esecuzione di tutte le funzioni che accettano come
 * paramento l'oggetto lanciato come evento in un ordine definito.
 *
 * Le funzioni da chiamare devono essere registrate.
 * Per essere registrate queste ultime devono essere contenute
 * in una classe che implementa "ListenerGame".
 *
 * La registrazione viene effettuata come segue:
 * Quando si registra una classe che contiene l'esecuzione di un
 * evento, si fa:
 * - Si prendono tutte le funzione in essa contenuta, filtrare
 *   queste se hanno l'annotazione "@EventHandlerGame".
 * - Ulteriore filtro devono avere solo 1 parametro.
 * - Se il paramentro non è mappato questo viene inserito come
 *   chiave di una mappa del tipo "ClassType -> Lista<ListenerGame>"
 * - Ogni classe che viene registrata deve implementare l'interfaccia "ListenerGame"
 * - Ogni Oggetto evento deve Estendere la classe astratta "EventGame"
 * - Gli eventi concreti possono implementare l'interfaccia Cancellable
 * - Gli eventi hanno una priorità di chiamata, questa viene generata
 *   mettendo in ordine gli oggetti dentro la lista mappata citata sopra
 * - Data la possibile presenza di piu funzioni per lo stesso evento la
 *   lista è composta da oggetti che comprendono queste informazioni:
 *      - Oggetto della classe
 *      - Funzione da chiamare
 *      - Ignora la cancellazione
 * - La priorità è contenuta nell'annotazione
 * - Sempre nell'annotazione è presente la clausola se ignorare che
 *   l'evento è stato cancellato
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.1
 ****************************************************************/
object EventManager {

    private var map : HashMap<KClass<out EventGame>, MutableList<FunctionToCall>> = HashMap()

    /**
     * Registra l'oggetto Listener come ascoltatore degli eventi che verranno lanciati
     */
    fun registerEvent(list : ListenerGame){
        val cl = list::class

        //filtra le funzioni tramite le annotazioni e il numero di parametri richiesti dalla funzione
        val fn = cl.memberFunctions.filter {
            it.annotations.any { annotation -> annotation is EventHandlerGame }.and(it.parameters.size == 2)
        }

        //ora ho tutte funzioni che possono essere registrate.
        //ciclo le funzioni
        for(toReg in fn){
            //prendo il tipo che richiede
            val param : KClass<out EventGame> = toReg.parameters[1].type.jvmErasure as KClass<out EventGame>

            //controllo se nella mappa è presente come chiave
            if(this.map.containsKey(param)){
                //si è presente aggiungi alla lista seguendo la priorità
                val lt = this.map[param]
                lt?.add(FunctionToCall(list, toReg))
                lt?.sort()
            }else{
                //non era presente la chiave
                val lt = ArrayList<FunctionToCall>()
                lt.add(FunctionToCall(list, toReg))
                this.map[param] = lt
            }
        }
    }

    /**
     * Chiama l'evento
     */
    fun callEvent(event : EventGame){
        val funcs = this.map[event::class]
        if(funcs != null){
            for(fnc in funcs){
                //controlla se implementa Cancellable
                if(event is Cancellable){
                    //è di tipo Cancellable
                    if(event.isCancelled()){
                        //è stato cancellato
                        //controlla se ha l'annotazione per ignorare una eventuale cancellazione, memorizzata nella fnc.
                        if(!fnc.ignore){
                            //non deve ignorare la cancellazione
                            continue
                        }
                    }
                }
                fnc.fn.call(fnc.cl, event)
            }
        }
    }

    /**
     * elimina tutte le classi listener registrate
     */
    fun unRegisterAll() {
        map.clear()
    }
}






