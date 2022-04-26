package it.elsalamander.mine3d.Game.Graphic.V2

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import it.elsalamander.mine3d.Game.Game.Data.GameInstance
import it.elsalamander.mine3d.Game.Settings.BaseSettings.Theme.ThemeList
import it.elsalamander.mine3d.R
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRendereV2(var game : GameInstance) : GLSurfaceView.Renderer {

    var width : Int = 0
    var height : Int = 0

    var mTotalDeltaX = 0f
    var mTotalDeltaY = 0f
    var mTotalDeltaZ = 0f
    var zoom = 0.2f

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



    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //Imposta il colore di sfondo
        //GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)

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

        GLCubeBaseV2.tmp_id_texture = GLCubeBaseV2.createGLSprite(game.context, R.drawable.nuno)

        ShaderHelper.initGlProgram();
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height

        //serve per contrare la matrice nel centro dello schermo
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 8f)

        this.width = width
        this.height = height

        GLES30.glUseProgram(ShaderHelper.programTexture);
    }

    override fun onDrawFrame(gl: GL10?) {

        //Ridisegna lo sfondo
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

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
                val myCube = it.getVal()?.second
                val cube = /*myCube?.glCube*/ GLCubeBaseV2

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

                myCube?.xRend = tmpM[12]
                myCube?.yRend = tmpM[13]
                myCube?.zRend = tmpM[14]
                myCube?.dist  = tmpM[15]

                if (cube != null) {
                    cube.draw(tmpM)
                }
            }
            //return@visitLeaf
        }
    }
}