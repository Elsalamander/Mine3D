package com.example.mine3d.Game.Graphic

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.example.mine3d.Game.Game.Data.GameInstance

/****************************************************************
 * Oggetto per descrivere la SurfaceView per creare il Render
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class MyGLSurfaceView(var cont: Context, var game: GameInstance) : GLSurfaceView(cont) {

    var mRenderer: MyRenderer

    private var mPreviousX = 0f
    private var mPreviousY = 0f
    private var mScaleDetector : ScaleGestureDetector
    private var scale : ScaleDetector

    private var oldAng = -100.0

    init{
        setEGLContextClientVersion(2)
        mRenderer = MyRenderer(game)
        setRenderer(mRenderer)
        renderMode = RENDERMODE_WHEN_DIRTY

        scale = ScaleDetector(this)
        mScaleDetector = ScaleGestureDetector(context, scale)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //evento per lo zoom
        if(mScaleDetector.onTouchEvent(event)){
            requestRender()
        }

        val x = event.x
        val y = event.y

        //Gestione rotazione asse Z
        val x2 = event.getX(event.pointerCount-1)
        val y2 = event.getY(event.pointerCount-1)
        if(x != x2){

            //chi è a sinistra?
            var x_1 = x
            var y_1 = y
            var x_2 = x2
            var y_2 = y2


            var newAng = Math.atan(((y_2-y_1)/(x_2-x_1)).toDouble())

            if(x_1 == x_2){
                oldAng = -100.0
            }

            if(oldAng == -100.0){
                oldAng = newAng
            }else{
                mRenderer.mDeltaZ += ((oldAng - newAng) * 201).toFloat()
                oldAng = newAng
                mRenderer.mTotalDeltaZ += mRenderer.mDeltaZ
                mRenderer.mTotalDeltaZ = mRenderer.mTotalDeltaZ % 360
                requestRender()
            }
        }else{
            oldAng = -100.0

            //Gestione rotazione X e Y
            if (event.action == MotionEvent.ACTION_MOVE) {
                val deltaX = (x - mPreviousX) / 2f
                val deltaY = (y - mPreviousY) / 2f
                mRenderer.mDeltaX += deltaX
                mRenderer.mDeltaY += deltaY
                mRenderer.mTotalDeltaX += mRenderer.mDeltaX
                mRenderer.mTotalDeltaY += mRenderer.mDeltaY
                mRenderer.mTotalDeltaX = mRenderer.mTotalDeltaX % 360
                mRenderer.mTotalDeltaY = mRenderer.mTotalDeltaY % 360
                requestRender()
            }
        }
        mPreviousX = x
        mPreviousY = y


        //ritorna true perchè ho gestito l'evento
        return true
    }

}