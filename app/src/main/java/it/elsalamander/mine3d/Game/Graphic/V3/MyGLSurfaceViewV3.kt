package it.elsalamander.mine3d.Game.Graphic.V3

import android.opengl.GLSurfaceView
import android.view.ScaleGestureDetector
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Graphic.Engine.MyRenderer
import it.elsalamander.mine3d.Game.Graphic.Engine.ScaleDetector

class MyGLSurfaceViewV3(var game: GameInstance) : GLSurfaceView(game.context) {

    var mRenderer: GLES20TriangleRenderer

    private var mPreviousX = 0f
    private var mPreviousY = 0f

    private var oldAng = -100.0

    init{
        setEGLContextClientVersion(2)
        mRenderer = GLES20TriangleRenderer(game.context)
        setRenderer(mRenderer)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }



}