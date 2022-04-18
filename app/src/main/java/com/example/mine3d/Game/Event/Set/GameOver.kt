package com.example.mine3d.Game.Event.Set

import com.example.mine3d.Game.Event.Manager.EventGame

class GameOver(var upperEvent : RevealBombEvent) : EventGame(upperEvent.instanceGame) {
}