package it.elsalamander.mine3d.Game.Media.Event.Set

import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Media.Event.MediaEvent

/****************************************************************
 * Classe evento quando una musica/effetto inizia.
 *
 * @author: Elsalamander
 * @data: 28 aprile 2021
 * @version: v1.0
 ****************************************************************/
class StartSongEvent(game : GameInstance, var id : Int) : MediaEvent(game) {
}