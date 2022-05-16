package it.elsalamander.mine3d.Game.Graphic.Engine

import android.opengl.GLES20
import android.opengl.GLU
import java.nio.FloatBuffer
import java.nio.IntBuffer


class CubeDetectSupport(val render : MyRenderer) {



    fun prova(x : Int, y :Int): FloatArray {
        val winX = 2f * x/render.width - 1f
        val winY = 1f - 2f * y/render.height

        val winZ = FloatBuffer.allocate(1)
        GLES20.glReadPixels(x, y, 1, 1, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_FLOAT, winZ)
        val coords = FloatArray(4)

        val viewPort = IntBuffer.allocate(4)
        GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, viewPort)

        GLU.gluUnProject(winX, winY, winZ[0], render.mViewMatrix, 0, render.mProjectionMatrix,0,
            viewPort.array(), 0, coords,0)

        return coords
    }
}