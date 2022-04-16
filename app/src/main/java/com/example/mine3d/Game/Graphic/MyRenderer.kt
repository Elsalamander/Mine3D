package com.example.mine3d.Game.Graphic

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.example.mine3d.Game.Game.Data.GameInstance
import com.example.mine3d.Game.Settings.BaseSettings.Theme.ThemeList
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/****************************************************************
 * Il rendering varia a seconda dello stato del gioco, bisogna avere
 * una istanza del game corrente che contiene lo stato, e poi fare il draw di
 * tutti i cubi a seconda dello stato corrente.
 *
 * Oltre a dipendere dallo stato c'è da tenere in considerazione lo spostamento/rotazione
 * del "mega-cubo"
 * "mega-cubo" : si intende l'insieme di tutti i piccoli cubi, che di persè disegnano un grande cubo
 * Per disegnare il cubo devo sapere:
 * - Stato di esso  -> istanza gioco
 * - Coordinate relative -> istanza gioco
 * - Punto di vista corrente -> classe corrente
 *
 * Per tenere traccia/ruotare il tutto devo tenere da parte la rotazione effettuata
 * tramite tocco singolo.
 *
 * Ho 2 tipi di rotazioni:
 * - tramite l'evento onTouch posso ruotare l'asse X e Y, ma non la Z
 * - ruotare la Z quando ho 2 punti di Touch (Evento onTouchEvent, ma specificando il pointer 0 e 1)
 * - zoom tramite "ScaleGestureDetector.OnScaleGestureListener"
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class MyRenderer(var game : GameInstance) : GLSurfaceView.Renderer {

    var mTotalDeltaX = 0f
    var mTotalDeltaY = 0f
    var mTotalDeltaZ = 0f
    var zoom = 0.1f

    @Volatile
    var mDeltaX = 0f

    @Volatile
    var mDeltaY = 0f

    @Volatile
    var mDeltaZ = 0f

    // mMVPMatrix è una abbreviazione di "Model View Projection Matrix"
    private val mMVPMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    //crea un'altra matrice di rotazione
    private val mRotationMatrix = FloatArray(16)

    //memoria per la rotazione cumulativa
    private val mAccumulatedRotation = FloatArray(16)

    //memoria per la corrente rotazione
    private val mCurrentRotation = FloatArray(16)

    //matrice temporanea per fare il draw
    private val mTemporaryMatrix = FloatArray(16)

    private val vertexShaderCode =  // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +  //"attribute vec4 vColor;" +
                //"varying vec4 vColorVarying;" +
                "void main() {" +  // the matrix must be included as a modifier of gl_Position
                // Note that the uMVPMatrix factor *must be first* in order
                // for the matrix multiplication product to be correct.
                "  gl_Position = uMVPMatrix * vPosition;" +  //"vColorVarying = vColor;"+
                "}"

    private val fragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +  //"varying vec4 vColorVarying;"+
            "void main() {" +  //"  gl_FragColor = vColorVarying;" +
            "  gl_FragColor = vColor;" +
            "}"


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //Imposta il colore di sfondo
        //GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        gl!!.glDisable(GL10.GL_DITHER)
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)

        //gestione sfondo
        if(game.settings.baseSett.theme.getVal().equals(ThemeList.LIGHT)){
            gl.glClearColor(1f, 1f, 1f, 0.2f)
        }else if(game.settings.baseSett.theme.getVal().equals(ThemeList.DARK)){
            gl.glClearColor(0f, 0f, 0f, 0.2f)
        }else{
            //tema rosso, per indicare un errore
            gl.glClearColor(1f, 0f, 0f, 0.2f)
        }

        //Inizializza la matrice della rotazione
        Matrix.setIdentityM(mAccumulatedRotation, 0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height

        //serve per contrare la matrice nel centro dello schermo
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 8f)
    }

    override fun onDrawFrame(gl: GL10?) {

        //Ridisegna lo sfondo
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        val scratch = FloatArray(16)
        //Fai la rotazione per i cubi
        Matrix.setIdentityM(mRotationMatrix, 0)
        Matrix.setIdentityM(mCurrentRotation, 0)
        Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f)
        Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f)
        Matrix.rotateM(mCurrentRotation, 0, mDeltaZ, 0.0f, 0.0f, 1.0f)
        mDeltaX = 0.0f
        mDeltaY = 0.0f
        mDeltaZ = 0.0f

        //Moltiplica la corrente rotazione per la rotazione accumulata, e setta nella matrice la nuova
        //matrice ottenuta.
        Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0)
        System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16)

        //Ruota il cubo
        Matrix.multiplyMM(mTemporaryMatrix, 0, mRotationMatrix, 0, mAccumulatedRotation, 0)
        System.arraycopy(mTemporaryMatrix, 0, mRotationMatrix, 0, 16)

        //Imposta la posizione della camera
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 4f, 0f, 0f, 0f, 0f, 1f, 0f)

        //Calcola la proiezione della camera
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

        //Combina la matrice di rotazione con la proiezione della camera
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0)

        //matrice temporanea
        val tmpM = FloatArray(16)
        //var cube : GLCubeBase
        val N = game.grid.N

        //Visita tutti i nodi e fai il render di ognuno
        game.grid.visitLeaf {
            if (it != null) {
                val coord = it.getPoint()
                val cube = it.getVal()?.second?.glCube

                //Prendi il Cubo alla posizione x,y,z
                System.arraycopy(scratch, 0, tmpM, 0, 16)

                val x = coord.getAxisValue(0).toFloat()
                val y = coord.getAxisValue(1).toFloat()
                val z = coord.getAxisValue(2).toFloat()

                //trasla la matrice alle coordinate date
                val x_ = (x - (N - 1f) / 2f) * 1.5f * zoom
                val y_ = (y - (N - 1f) / 2f) * 1.5f * zoom
                val z_ = (z - (N - 1f) / 2f) * 1.5f * zoom
                Matrix.translateM(tmpM, 0, x_, y_, z_)

                //Esegui lo scalamneto dello zoom
                Matrix.scaleM(tmpM, 0, zoom, zoom, zoom)

                val shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, vertexShaderCode)
                GLES20.glCompileShader(shader)

                val framm = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)

                // add the source code to the shader and compile it
                GLES20.glShaderSource(framm, fragmentShaderCode)
                GLES20.glCompileShader(framm)

                if (cube != null) {
                    val programGL = cube.createGlProgram(shader, framm)
                    cube.draw(tmpM, programGL)
                }
            }
        }
    }
}
