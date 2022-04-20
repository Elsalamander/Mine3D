package it.elsalamander.mine3d.Game.Event.Set

import it.elsalamander.mine3d.Game.Event.Manager.EventGame

class GameOverEvent(var upperEvent : RevealBombEvent) : EventGame(upperEvent.instanceGame) {
}