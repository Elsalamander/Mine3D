package com.example.mine3d.Game.Graphic

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/****************************************************************
 * Oggetto unico che descrive la matrice di base dei
 * GLCube.
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
object GLCubeBase {

    private var vertexBuffer: FloatBuffer? = null

    //Numero di coordinate vertex nell'array
    private const val COORDS_PER_VERTEX = 3

    private var cubeCoords = floatArrayOf(
        -0.5f,  0.5f,  0.5f,  // FTL 0
        -0.5f, -0.5f,  0.5f,  // FBL 1
         0.5f, -0.5f,  0.5f,  // FBR 2
         0.5f,  0.5f,  0.5f,  // FTR 3
        -0.5f,  0.5f, -0.5f,  // BTL 4
         0.5f,  0.5f, -0.5f,  // BTR 5
        -0.5f, -0.5f, -0.5f,  // BBR 6
         0.5f, -0.5f, -0.5f
    )

    //ordine di scrittura dei vertici
    private val drawOrder = arrayOf(
        shortArrayOf(0, 1, 2, 0, 2, 3),
        shortArrayOf(0, 4, 5, 0, 5, 3),
        shortArrayOf(0, 1, 6, 0, 6, 4),
        shortArrayOf(3, 2, 7, 3, 7, 5),
        shortArrayOf(1, 2, 7, 1, 7, 6),
        shortArrayOf(4, 6, 7, 4, 7, 5)
    )

    //colore dei cubi, per debug
    private val cubeColor3 = arrayOf(
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f
        ), floatArrayOf(
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f
        ), floatArrayOf(
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
        ), floatArrayOf(
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f
        ), floatArrayOf(
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f
        ), floatArrayOf(
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f
        )
    )

    private var mPositionHandle = 0
    private var mColorHandle = 0

    private const val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex


    init{
        //inizliazza il vertex buffer per le coordinate
        val bb = ByteBuffer.allocateDirect(cubeCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(cubeCoords)
        vertexBuffer?.position(0)
    }

    /**
     * Disegna il cubo con i parametri passati
     */
    fun draw(mvpMatrix: FloatArray?, mProgram : Int) {
        var mMVPMatrixHandle = 0
        //disegna le faccie
        for (face in 0..5) {

            //Aggiungi il programma openGL
            GLES20.glUseProgram(mProgram)

            //prendi la posizione
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")


            //prendi i colori
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")

            //abilita per usare i vertici del cubo
            GLES20.glEnableVertexAttribArray(mPositionHandle)
            //prepara le coordinate dei vertici
            GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer
            )
            //crea il buffer per le coordinate
            val dlb = ByteBuffer.allocateDirect(
                drawOrder[face].size * 2
            )
            dlb.order(ByteOrder.nativeOrder())
            val drawListBuffer = dlb.asShortBuffer()
            drawListBuffer.put(drawOrder[face])
            drawListBuffer.position(0)
            GLES20.glUniform4fv(mColorHandle, 1, cubeColor3[face], 0)

            //prendi la matrice per poi trasformarla
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

            //passa la matrice
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
            GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                drawOrder[face].size,
                GLES20.GL_UNSIGNED_SHORT,
                drawListBuffer
            )
        }

        //disabilita
        GLES20.glDisableVertexAttribArray(mMVPMatrixHandle)
    }

    /**
     * Crea il programma GL per poi fare il DRAW
     */
    fun createGlProgram(vertexShader : Int, fragmentShader : Int): Int {
        // create empty OpenGL ES Program
        val mProgram = GLES20.glCreateProgram()

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader)

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader)

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram)

        return mProgram
    }
}