package it.elsalamander.mine3d.Game.Media.Event

import it.elsalamander.mine3d.Game.Event.Manager.EventGame
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Media.SoundMedia

/****************************************************************
 * Classe per astrarre tutti gli eventi che riguardano i media
 *
 * @author: Elsalamander
 * @data: 28 aprile 2021
 * @version: v1.0
 ****************************************************************/
abstract class MediaEvent(game : GameInstance) : EventGame(game){
    val songManager : SoundMedia
        get() {
            return super.instanceGame.media
        }
}