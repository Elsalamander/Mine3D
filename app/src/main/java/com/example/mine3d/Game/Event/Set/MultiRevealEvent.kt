package com.example.mine3d.Game.Event.Set

import com.example.mine3d.Game.Event.Manager.EventGame

class MultiRevealEvent(var upperEvent : RevealCubeEvent) : EventGame(upperEvent.instanceGame) {
}