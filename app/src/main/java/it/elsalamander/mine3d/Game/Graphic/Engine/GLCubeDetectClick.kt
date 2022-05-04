package it.elsalamander.mine3d.Game.Graphic.Engine

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import it.elsalamander.mine3d.Game.Event.Set.PlaceFlagEvent
import it.elsalamander.mine3d.Game.Event.Set.RevealCubeEvent
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Graphic.MineCube
import it.elsalamander.mine3d.Game.Settings.ControlSettings.TypeTouch
import it.elsalamander.mine3d.util.Pair
import java.lang.Math.abs
import java.util.ArrayList

/****************************************************************
 * Classe per capire se sto premendo un cubo o no.
 *
 * Poichè tenere premuto un stesso punto è difficile, identifichero
 * come "stesso punto" se si discosta al massimo di un certo valore
 * sull'asse x e y.
 *
 * C'è da differenziare anche il fatto di preme "solo quel punto"
 * ovvero che premo e rilascio e non arrivarci "strascinando"
 * l'evento deve nascere e morire in quel punto per effettuare un
 * reveal o un flag.
 *
 * --------------------------------------------------------------
 * Soluzione temporanea, quando clicco faccio riferimento al cubo alle coordinate
 * x=0 e y=0
 *
 * Note:
 *  - Un gesto inizia con un evento di movimento con ACTION_DOWN
 *  - un gesto termina quando il puntatore finale sale come
 *    rappresentato da un evento di movimento con ACTION_UP o
 *    quando il gesto viene annullato con ACTION_CANCEL
 *
 * @author: Elsalamander
 * @data: 27 aprile 2021
 * @version: v1.0
 ****************************************************************/
class GLCubeDetectClick(var game: GameInstance,var mRenderer: MyRenderer) : View.OnTouchListener {

    companion object{
        const val dx : Float = 5f //scostamento massimo di x
        const val dy : Float = 5f //scostamento massimo di y
    }

    private var firstX : Float = 0f  //Primo valore della X
    private var firstY : Float = 0f  //Primo valore della Y
    private var firstCheck : Boolean = false    //TRUE se ho preso il primo valore della x e y, altrimenti FALSE
    private var valid : Boolean = false     //tiene conto se è valida la procedura o se ho sforato in un momento precedente
    private var timer : Long = 0  //timer, inizia a contare quando ho iniziato a premere
    private val holdTimer : Double  //valore da aspettare
        get() {
            return game.context.settings.controlSett.holdTimer.getVal() * 1000
        }


    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     * the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @SuppressLint("ClickableViewAccessibility") //l'accesso viene fatto, al difuori del mio codice
    override fun onTouch(v: View, event: MotionEvent): Boolean {

        //è il primo istante valido?
        if(event.action == MotionEvent.ACTION_DOWN && !firstCheck && !valid){
            //si è il primo istante di tocco
            firstX = event.x    //prendo il valore di x corrente
            firstY = event.y    //prendo il valore di y corrente

            firstCheck = true   //cambio lo stato
            valid = true        //attualmente valido come click

            timer = System.currentTimeMillis()    //prendo il timer
        }

        //possibile generazione di stato invalido, quando premo con un secondo dito
        if(event.pointerCount > 1){
            valid = false
        }

        //sono in uno stato valido del detect?
        if(firstCheck && valid){
            //prendo il valore di x e y
            val newX = event.x
            val newY = event.y

            //confronta con lo scostamento massimo se rientra nei parametri
            if(firstX - dx < newX && newX < firstX + dx){
                //asse X valido
            }else{
                //asse X non valido, invalida tutto
                valid = false
            }
            if(firstY - dy < newY && newY < firstY + dy){
                //asse Y valido
            }else{
                //asse Y non valido, invalida tutto
                valid = false
            }
        }

        //dopo aver controllato il nuovo stato, se è ancora valido controlla il time
        if(valid){
            if(System.currentTimeMillis() - timer > holdTimer){
                //il timer è andato oltre, voglio metterci una bandiera nel cubo in cui mi trovo
                //prendi il cubo
                //val pair = getCube(firstX, firstY)
                val pair = getCube(0f, 0f)
                if(pair != null){
                    val cube = pair.second
                    val coords = pair.first

                    //ci sono 2 casi
                    if(game.context.settings.controlSett.flag_sx_dx.getVal() == TypeTouch.Hold){
                        game.context.eventManager.callEvent(PlaceFlagEvent(game, coords[0], coords[1], coords[2], cube))
                    }
                    if(game.context.settings.controlSett.reveal_sx_dx.getVal() == TypeTouch.Hold){
                        game.context.eventManager.callEvent(RevealCubeEvent(game, coords[0], coords[1], coords[2], cube))
                    }
                }else{
                    //punto non valido, non c'è nessun cubo
                }

                //una volta lanciato questo evento invalido, perchè ho già chiamato l'evento
                //interessato al gioco
                valid = false
            }
        }

        //se rilascio, "smetto di premere", devo resettare tutti i valori
        if(event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){
            //prima di resettare esegui il reveal o flag
            if(System.currentTimeMillis() - timer < holdTimer && valid){
                //val pair = getCube(firstX, firstY)
                val pair = getCube(0f, 0f)
                if(pair != null){
                    val cube = pair.second
                    val coords = pair.first

                    //ci sono 2 casi
                    if(game.context.settings.controlSett.flag_sx_dx.getVal() == TypeTouch.Tap){
                        game.context.eventManager.callEvent(PlaceFlagEvent(game, coords[0], coords[1], coords[2], cube))
                    }
                    if(game.context.settings.controlSett.reveal_sx_dx.getVal() == TypeTouch.Tap){
                        game.context.eventManager.callEvent(RevealCubeEvent(game, coords[0], coords[1], coords[2], cube))
                    }
                }else{
                    //punto non valido, non c'è nessun cubo
                }
            }

            //ora posso resettare lo stato
            firstX = 0f
            firstY = 0f
            firstCheck = false
            valid = false
            timer = 0
        }
        return true
    }

    /**
     * Funzione che prende l'oggetto cubo premuto in base alle coordinate
     * passate.
     * Ritorna un Pair dove i valori sono:
     *  - first: array delle coordinate x,y,z della griglia, non quelli grafici!
     *  - seccond: oggetto mineCube alle coordinate date
     *
     *  ----------------------------------------------------
     *  Soluzione temporanea quando clicco faccio riferimento
     *  al cubo che si trova alle coordinate x=0 e y=0
     */
    private fun getCube(x : Float, y : Float) : Pair<IntArray, MineCube>?{
        var larghezza = 0f

        val rx = mRenderer.mTotalDeltaX
        val ry = mRenderer.mTotalDeltaY

        val select = when{
            ((rx<=45 || rx>=315) &&(ry<=45 || ry>=315)) -> 0 //cerca x=0, y=0 e z più grande
            ((rx>45  && rx<135)  &&(ry<=45 || ry>=315)) -> 1 //cerca x=0, z=0 e la y più piccola
            ((rx>135  && rx<225) &&(ry<=45 || ry>=315)) -> 2 //cerca x=0, y=0 e la z più piccola
            ((rx>225 && rx<315)  &&(ry<=45 || ry>=315)) -> 3 //cerca x=0, z=0 e la y più grande

            //ruoto la y di 90 gradi
            ((rx<=45 || rx>=315) &&(ry>45  && ry<135)) -> 4 //cerca z=0, y=0 e la x più piccola
            ((rx>45  && rx<135)  &&(ry>45  && ry<135)) -> 4 //cerca
            ((rx>135 && rx<225)  &&(ry>45  && ry<135)) -> 4 //cerca
            ((rx>225 && rx<315)  &&(ry>45  && ry<135)) -> 4 //cerca

            //ruoto la y di altri 90 gradi
            ((rx<=45 || rx>=315) &&(ry>135  && ry<225)) -> 2 //cerca x=0, y=0 e la z più piccola
            ((rx>45  && rx<135)  &&(ry>135  && ry<225)) -> 3 //cerca x=0, z=0 e la y più grande
            ((rx>135 && rx<225)  &&(ry>135  && ry<225)) -> 0 //cerca x=0, y=0 e z più grande
            ((rx>225 && rx<315)  &&(ry>135  && ry<225)) -> 1 //cerca x=0, z=0 e la y più piccola

            //ruoto la y di altri 90 gradi
            ((rx<=45 || rx>=315) &&(ry>225  && ry<315)) -> 5 //cerca z=0, y=0 e la x più grande
            ((rx>45  && rx<135)  &&(ry>225  && ry<315)) -> 5 //cerca
            ((rx>135 && rx<225)  &&(ry>225  && ry<315)) -> 5 //cerca
            ((rx>225 && rx<315)  &&(ry>225  && ry<315)) -> 5 //cerca

            else -> -1
        }

        if(select == -1){
            return null
        }

        var aM = 0f
        var bM = 0f
        var last : Pair<IntArray, MineCube>? = null
        //visita tutti i nodi per scoprire quale cubo si trova alle coordinate passate
        game.grid.visitLeaf {
            if(larghezza == 0f){
                larghezza = kotlin.math.abs(it?.getVal()?.second?.larghezza ?: 0f) * 1.25f
            }
            aM = when(select){
                in 0..3 -> it?.getVal()?.second?.xRend ?: 0f
                else -> it?.getVal()?.second?.yRend ?: 0f
            }
            bM = when(select){
                5,4,3,1 -> it?.getVal()?.second?.zRend ?: 0f
                else -> it?.getVal()?.second?.yRend ?: 0f
            }

            //rientra nelle coordinate passate?
            if(aM - larghezza <= x && x <= aM + larghezza){
                if(bM - larghezza <= y && y <= bM + larghezza){
                    //rientra, butta nella lista
                    val arr = IntArray(3)
                    arr[0] = it?.getPoint()?.getAxisValue(0)!!.toInt()
                    arr[1] = it.getPoint().getAxisValue(1).toInt()
                    arr[2] = it.getPoint().getAxisValue(2).toInt()

                    if(last != null){
                        if(select == 0 || select == 3 || select == 5){
                            if(last!!.first[2] <= arr[2]){
                                last = Pair(arr, it.getVal()!!.second)
                            }
                        }else{
                            if(last!!.first[2] > arr[2]){
                                last = Pair(arr, it.getVal()!!.second)
                            }
                        }
                    }else{
                        last = Pair(arr, it.getVal()!!.second)
                    }
                }
            }
        }

        Log.d("TouchEvent", "Cubo trovato $last")
        //ritorna il risultato finale
        return last
    }
}