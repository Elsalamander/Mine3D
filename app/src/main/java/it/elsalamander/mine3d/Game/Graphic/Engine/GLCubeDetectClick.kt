package it.elsalamander.mine3d.Game.Graphic.Engine

import android.view.MotionEvent
import android.view.View
import it.elsalamander.mine3d.Game.Event.Set.PlaceFlagEvent
import it.elsalamander.mine3d.Game.Event.Set.RevealCubeEvent
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Graphic.MineCube
import it.elsalamander.mine3d.Game.Settings.ControlSettings.TypeTouch

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
class GLCubeDetectClick(var game: GameInstance) : View.OnTouchListener {

    companion object{
        val dx : Float = 5f //scostamento massimo di x
        val dy : Float = 5f //scostamento massimo di y
    }

    var firstX : Float = 0f  //Primo valore della X
    var firstY : Float = 0f  //Primo valore della Y
    var firstCheck : Boolean = false    //TRUE se ho preso il primo valore della x e y, altrimenti FALSE
    var valid : Boolean = false     //tiene conto se è valida la procedura o se ho sforato in un momento precedente
    var timer : Long = 0  //timer, inizia a contare quando ho iniziato a premere
    val holdTimer : Double  //valore da aspettare
        get() {
            return game.settings.controlSett.holdTimer.getVal()
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
                val pair = getCube(firstX, firstY)
                if(pair != null){
                    val cube = pair.second
                    val coords = pair.first

                    //ci sono 2 casi
                    if(game.settings.controlSett.flag_sx_dx.getVal() == TypeTouch.Hold){
                        game.context.eventManager.callEvent(PlaceFlagEvent(game, coords[0], coords[1], coords[2], cube))
                    }
                    if(game.settings.controlSett.reveal_sx_dx.getVal() == TypeTouch.Hold){
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
            if(System.currentTimeMillis() - timer < holdTimer){
                val pair = getCube(firstX, firstY)
                if(pair != null){
                    val cube = pair.second
                    val coords = pair.first

                    //ci sono 2 casi
                    if(game.settings.controlSett.flag_sx_dx.getVal() == TypeTouch.Tap){
                        game.context.eventManager.callEvent(PlaceFlagEvent(game, coords[0], coords[1], coords[2], cube))
                    }
                    if(game.settings.controlSett.reveal_sx_dx.getVal() == TypeTouch.Tap){
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
     */
    private fun getCube(x : Float, y : Float) : Pair<IntArray, MineCube>?{
        return null
    }
}