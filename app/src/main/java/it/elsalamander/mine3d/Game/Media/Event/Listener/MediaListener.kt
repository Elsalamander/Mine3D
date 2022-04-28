package it.elsalamander.mine3d.Game.Media.Event.Listener

import android.util.Log
import it.elsalamander.mine3d.Game.Event.Manager.EventHandlerGame
import it.elsalamander.mine3d.Game.Event.Manager.EventPriority
import it.elsalamander.mine3d.Game.Event.Manager.ListenerGame
import it.elsalamander.mine3d.Game.Event.Set.GameStart
import it.elsalamander.mine3d.Game.Media.Event.Set.EndSongEvent
import it.elsalamander.mine3d.Game.Media.Event.Set.StartSongEvent

class MediaListener : ListenerGame {

    /**
     * Gestion dell'evento GameStart
     * Priorità di esecuzione bassa
     * Avvia la musica di sottofondo
     */
    @EventHandlerGame(EventPriority.LOW, true)
    fun onStartGame(event : GameStart){
        //avvia la musica
        Log.d("Music", "PlayStartGame")
        if(event.instanceGame.media.getCurrentSong() == null){
            Log.d("Music", "PlayStartGame avvia nuova canzone")
            event.instanceGame.media.playSong()
        }
    }

    @EventHandlerGame
    fun onStartSong(event : StartSongEvent){
        val song = event.songManager.getSong(event.id)

        Log.d("Music", "Start: ${event.id}")

        //imposta il volume
        val vol = event.songManager.getVolume()
        song?.setVolume(vol, vol)

        //avvia la musica
        song?.start()
    }

    @EventHandlerGame
    fun onEndSong(event : EndSongEvent){

        Log.d("Music", "Stop: ${event.id}")

        //avvia la prossima musica
        if(event.songManager.getCurrentSong()!= null){
            event.songManager.playSong()
        }
    }
}