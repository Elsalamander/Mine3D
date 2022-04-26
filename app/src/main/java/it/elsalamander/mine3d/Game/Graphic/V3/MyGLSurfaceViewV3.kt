package it.elsalamander.mine3d.Game.Graphic.V3

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Graphic.V2.MyRendereV2

class MyGLSurfaceViewV3(var cont: Context, var game: GameInstance) : GLSurfaceView(cont) {

    var mRenderer: GLES20TriangleRenderer

    private var mPreviousX = 0f
    private var mPreviousY = 0f

    private var oldAng = -100.0

    init{
        setEGLContextClientVersion(2)
        mRenderer = GLES20TriangleRenderer(game.context)
        setRenderer(mRenderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }



}