package it.elsalamander.mine3d.Game.Graphic.Engine

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Game.Game
import it.elsalamander.mine3d.R
import kotlin.math.PI
import kotlin.math.atan

/****************************************************************
 * Classe che contiene il renderer e gestisce il tocco quando
 * esso è attivo, per ruotare e zommare.
 *
 * Manca da fare: -Determinare il cubo premuto.
 *
 * @author: Elsalamander
 * @data: 16 aprile 2021
 * @version: v2.0
 ****************************************************************/
class MyGLSurfaceView(var cont : Context, var attrs : AttributeSet) : GLSurfaceView(cont, attrs)  {
    var mRenderer: MyRenderer

    private var game : GameInstance = (cont as Game).gameInstance!!
    private var mPreviousX = 0f
    private var mPreviousY = 0f
    private var mScaleDetector : ScaleGestureDetector
    private var scale : ScaleDetector
    private var touchDetector : GLCubeDetectClick

    private var oldAng = -100.0

    init{
        setEGLContextClientVersion(2)
        mRenderer = MyRenderer(game)
        setRenderer(mRenderer)
        renderMode = RENDERMODE_WHEN_DIRTY

        scale = ScaleDetector(this)
        mScaleDetector = ScaleGestureDetector(context, scale)
        touchDetector = GLCubeDetectClick(game)
    }

    @SuppressLint("ClickableViewAccessibility") //chiamata fatta aldifuori del mio codice
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //evento di detect
        touchDetector.onTouch(this, event)

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
            val x_1 = x
            val y_1 = y
            val x_2 = x2
            val y_2 = y2


            val newAng = atan(((y_2-y_1)/(x_2-x_1)).toDouble())

            if((x_1 == x_2) or ((newAng < -PI/4) and (oldAng > PI/4)) or ((newAng > PI/4) and (oldAng < -PI/4))){
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
            mPreviousX = x
            mPreviousY = y
        }

        //Log.d("Touch", "coordiante x: $x , y: $y")
        val w = mRenderer.width
        val h = mRenderer.height
        val ndcX = 2.0 * x/w - 1.0
        val ndcY = 1.0 - 2.0 * y/h
        //Log.d("Touch", "coordiante x_ndc: $ndcX , y_ndc: $ndcY")

        //ritorna true perchè ho gestito l'evento
        return true
    }
}