package it.elsalamander.mine3d.Game.Media.Event.Listener

import android.util.Log
import it.elsalamander.mine3d.Game.Event.Manager.EventHandlerGame
import it.elsalamander.mine3d.Game.Event.Manager.EventPriority
import it.elsalamander.mine3d.Game.Event.Manager.ListenerGame
import it.elsalamander.mine3d.Game.Event.Set.GameOverEvent
import it.elsalamander.mine3d.Game.Event.Set.GameStart
import it.elsalamander.mine3d.Game.Event.Set.QuitGameEvent
import it.elsalamander.mine3d.Game.Event.Set.RevealCubeEvent
import it.elsalamander.mine3d.Game.Media.Event.Set.EndSongEvent
import it.elsalamander.mine3d.Game.Media.Event.Set.StartSongEvent

/****************************************************************
 * Classe listener per eseguire le chiamate ad evento per
 * i media
 *
 * @author: Elsalamander
 * @data: 28 aprile 2021
 * @version: v1.0
 ****************************************************************/
class MediaListener : ListenerGame {

    /**
     * Gestion dell'evento GameStart
     * Priorità di esecuzione bassa
     * Lancia l'evento per avviare la musica di sottofondo
     */
    @EventHandlerGame(EventPriority.LOW, true)
    fun onStartGame(event : GameStart){
        //avvia la musica
        if(event.instanceGame.context.media.getCurrentSong() == null){
            event.instanceGame.context.media.playSong()
        }
    }

    /**
     * Evento che avvia in modo effettivo la musica di
     * sottofondo
     */
    @EventHandlerGame
    fun onStartSong(event : StartSongEvent){
        val song = event.songManager.getSong(event.id)

        //Log.d("Music", "Start: ${event.id}")

        //imposta il volume
        val vol = event.songManager.getVolume()
        song?.setVolume(vol, vol)

        //avvia la musica
        song?.start()
    }

    /**
     * Evento chiamanto quando la musica si conclude
     */
    @EventHandlerGame
    fun onEndSong(event : EndSongEvent){

        //Log.d("Music", "Stop: ${event.id}")

        //avvia la prossima musica
        if(event.songManager.getCurrentSong()!= null){
            event.songManager.playSong()
        }
    }

    /**
     * Ferma la musica quando si esce dal game
     */
    @EventHandlerGame(EventPriority.LOW, true)
    fun onQuitGame(event : QuitGameEvent){
        event.instanceGame.context.media.stop()
    }

    /**
     * Quando si perde esegui l'effetto sonoro della bomba
     */
    @EventHandlerGame(EventPriority.LOW)
    fun onGameOver(event : GameOverEvent){
        event.instanceGame.context.media.playBombEffect(event.instanceGame)
    }

    /**
     * Quando si scopre esegui l'effetto sonoro di "scopritura"
     * Se questo evento non è stato precedentemente cancellato,
     * dato che ci sono 3 motivi per cui questo evento viene
     * cancellato e dunque non viene chiamata giustamnte la funzione.
     * Questo grazie alla priorità che è più bassa delle altre che
     * cancellano l'evento.
     */
    @EventHandlerGame(EventPriority.LOW)
    fun onGameOver(event : RevealCubeEvent){
        event.instanceGame.context.media.playRevealEffect(event.instanceGame)
    }
}