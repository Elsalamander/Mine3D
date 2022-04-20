package it.elsalamander.mine3d.Game.Event.Set

import it.elsalamander.mine3d.Game.Event.Manager.EventGame

class WinEvent(var upperEvent : RevealCubeEvent) : EventGame(upperEvent.instanceGame) {
}