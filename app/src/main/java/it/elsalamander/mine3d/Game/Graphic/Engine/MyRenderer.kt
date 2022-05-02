package it.elsalamander.mine3d.Game.Graphic.Engine

import android.annotation.SuppressLint
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

/****************************************************************
 * Classe per realizzare il renderer del gioco
 * Gestisce il rendering del gioco, in particolare l'insieme dei
 * cubi presenti, con il supporto della classe object GLCube
 *
 * Ottimizzazioni/Log:
 * V 2.1: Data di modifica 26/04/2020
 *        Il codice realizza correttamente il rendering dei cubi
 *        NON sono state effettuate ottimizzazioni o accorgimenti
 *        particolari.
 *
 *
 *
 *
 * **Nota: le versioni precedenti alla 2.1 non eseguivano il texturing
 *         o comunque non realizzava l'obbiettivo, non ho tenuto in
 *         considerazione.
 *
 *
 * @author: Elsalamander
 * @data: 16 aprile 2021
 * @version: v2.1
 ****************************************************************/
class MyRenderer(var game : GameInstance) : GLSurfaceView.Renderer{

    var width : Int = 0
    var height : Int = 0

    var mTotalDeltaX = 0f
    var mTotalDeltaY = 0f
    var mTotalDeltaZ = 0f
    var zoom = 0.25f

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


    @SuppressLint("ResourceType")
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        //Inizializza la matrice della rotazione
        Matrix.setIdentityM(mAccumulatedRotation, 0)

        //crea il programma e carica le texture
        val theme = game.context.settings.baseSett.theme.getVal()
        val r = theme.red.toFloat()
        val g = theme.green.toFloat()
        val b = theme.blue.toFloat()
        GLCube.createSurfaceView(game.context, r, g, b, 1f)

        //punto di vista
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 4f, 0f, 0f, 0f, 0f, 1f, 0f)

        //Inizializza la matrice della rotazione
        Matrix.setIdentityM(mAccumulatedRotation, 0)
    }

    override fun onSurfaceChanged(glUnused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        this.width = width
        this.height = height

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
    }

    override fun onDrawFrame(gl: GL10?) {
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

        val n = game.grid.N

        //Visita tutti i nodi e fai il render di ognuno
        //Log.d("Renderer", "Rotazione X: $mTotalDeltaX")
        //Log.d("Renderer", "Rotazione Y: $mTotalDeltaY")

        game.grid.visitLeaf {
            if (it != null) {
                val coord = it.getPoint()
                val myCube = it.getVal()?.second

                //Prendi il Cubo alla posizione x,y,z
                System.arraycopy(scratch, 0, tmpM, 0, 16)

                val x = coord.getAxisValue(0).toFloat()
                val y = coord.getAxisValue(1).toFloat()
                val z = coord.getAxisValue(2).toFloat()

                //trasla la matrice alle coordinate date
                val x_ = (x - (n - 1f) / 2f) * 2.5f * zoom
                val y_ = (y - (n - 1f) / 2f) * 2.5f * zoom
                val z_ = (z - (n - 1f) / 2f) * 2.5f * zoom
                Matrix.translateM(tmpM, 0, x_, y_, z_)

                //Esegui lo scalamneto dello zoom
                Matrix.scaleM(tmpM, 0, zoom, zoom, zoom)

                myCube?.larghezza = tmpM[11]
                myCube?.xRend = tmpM[12]
                myCube?.yRend = tmpM[13]
                myCube?.zRend = tmpM[14]
                myCube?.dist  = tmpM[15]

                //var str = ""
                //var norm = 0f
                //for(i in 0 until 16){
                    //norm += tmpM[i] * tmpM[i]
                //}
                //norm = sqrt(norm)
                //norm = 1f
                //for(i in 11 until 16){
                    //str += "${tmpM[i]/norm}  "
                //}
                //Log.d("Cubo", "coords: $str")

                //disegna il cubo
                GLCube.draw(tmpM, myCube?.getTextureID() ?: 9)
            }
        }
    }
}