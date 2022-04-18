package com.example.mine3d.Game.Event.Set

import com.example.mine3d.Game.Event.Manager.EventGame
import com.example.mine3d.Game.Game.Data.GameInstance
import com.example.mine3d.Game.Graphic.MineCube

class PlaceFlagEvent(instanceGame : GameInstance, var x : Int, var y : Int, var z :Int, var cube : MineCube) : EventGame(instanceGame) {
}