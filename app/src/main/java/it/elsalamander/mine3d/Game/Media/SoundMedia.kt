package it.elsalamander.mine3d.Game.Media

import android.media.MediaPlayer
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Media.Event.Listener.MediaListener
import it.elsalamander.mine3d.Game.Media.Event.Set.EndSongEvent
import it.elsalamander.mine3d.Game.Media.Event.Set.StartSongEvent
import it.elsalamander.mine3d.R

/****************************************************************
 * Classe per gestire i vari Sound del gioco tra cui:
 * - Musica.
 * - Effetti speciali.
 *
 *
 * @author: Elsalamander
 * @data: 28 aprile 2021
 * @version: v1.0
 ****************************************************************/
class SoundMedia(var game : GameInstance) {

    private val options = game.settings
    private val songs = arrayOf(
        MediaPlayer.create(game.context, R.raw.song1),
        MediaPlayer.create(game.context, R.raw.song2)
    )

    private var state = -1 //stato: -1 non riproduce, 0: riproduce A, 1: riproduce B

    init{
        //configuro l'eventManager con i nuovi eventi
        game.context.eventManager.registerEvent(MediaListener())

        //setto l'evento da lancare quando una musica finisce
        for(song in songs){
            song.setOnCompletionListener {
                game.context.eventManager.callEvent(EndSongEvent(game,state))
            }
        }
    }

    /**
     * Ritorna il volume da mettere alla musica
     */
    fun getVolume() : Float {
        return options.baseSett.musicLevel.getVal()/100f
    }

    /**
     * Esegui una musica, o la prossima in base allo stato
     */
    fun playSong(){
        if(state == -1){
            state = 0
        }else{
            state++
        }

        if(state >= songs.size){
            state = 0
        }

        game.context.eventManager.callEvent(StartSongEvent(game,state))
    }

    /**
     * Metti in pausa la corrente musica
     */
    fun pause(){
        if(state != -1 && state < songs.size){
            if(songs[state].isPlaying){
                songs[state].pause()
            }
        }
    }

    /**
     * Riprendi la musica messa in pausa
     */
    fun reasume(){
        if(state != -1 && state < songs.size){
            if(!songs[state].isPlaying){
                songs[state].start()
            }
        }
    }

    /**
     * Ferma la musica corrente
     */
    fun stop(){
        if(state != -1 && state < songs.size){
            val tmp = state
            state = -1
            songs[tmp].stop()
        }
    }

    /**
     * Ritorna la musica all'indice id
     */
    fun getSong(id : Int) : MediaPlayer?{
        if(id >= 0 && id < songs.size){
            return songs[id]
        }
        return null
    }

    /**
     * Ritorna la musica corrente
     */
    fun getCurrentSong() : MediaPlayer?{
        return getSong(state)
    }

}