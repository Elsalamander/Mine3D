package com.example.mine3d.Game.Event.Set

import com.example.mine3d.Game.Event.Manager.EventGame

class WinEvent(var upperEvent : RevealCubeEvent) : EventGame(upperEvent.instanceGame) {
}