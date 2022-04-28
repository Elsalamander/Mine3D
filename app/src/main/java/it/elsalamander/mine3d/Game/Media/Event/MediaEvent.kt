package it.elsalamander.mine3d.Game.Media.Event

import it.elsalamander.mine3d.Game.Event.Manager.EventGame
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Media.SoundMedia

abstract class MediaEvent(game : GameInstance) : EventGame(game){
    val songManager : SoundMedia
        get() {
            return super.instanceGame.media
        }
}