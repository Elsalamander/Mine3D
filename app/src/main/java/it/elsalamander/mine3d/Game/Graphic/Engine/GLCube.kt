package it.elsalamander.mine3d.Game.Graphic.Engine

//import android.opengl.GLES20
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES32
import android.opengl.GLUtils
import android.util.Log
import it.elsalamander.mine3d.R
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/****************************************************************
 * Classe per aiutare il renderer del gioco.
 *
 * Ottimizzazioni/Log:
 * 2.0: Il codice realizza correttamente il rendering dei cubi
 *      non sono state effettuate ottimizzazioni o accorgimenti
 *      particolari.
 *
 * 2.1: E' stata modificata la OpenGL usata da GLES20 si è passati
 *      alla GLES32.
 *      Sono state commentate le righe sostituite
 *
 * 2.2: E' stata realizzata la funzione JNI per il draw, ma non
 *      funziona, è presente solo la funzione che esegue correttamente
 *      la chiamata in JNI.
 *
 * 2.3: E' stata aggiunta la chiamata "GLES32.glEnable(GLES32.GL_CULL_FACE)"
 *      nella funzione "createSurfaceView", per migliorare le prestazioni.
 *      Sono state rimosse le chiamate a funzione "checkGlError" nella
 *      funzione "draw" per migliorare le prestazioni
 *
 *
 *
 *
 * **Nota: le versioni precedenti alla 2.1 non eseguivano il texturing
 *         o comunque non realizzava l'obbiettivo, non ho tenuto in
 *         considerazione.
 *
 * @author: Elsalamander
 * @data: 16 aprile 2021
 * @version: v2.3
 ****************************************************************/
object GLCube {

    private val cubeVerticesStrip = floatArrayOf(
        -1f, -1f,  1f,  1f,
        -1f,  1f, -1f,  1f,
         1f,  1f,  1f,  1f,
         1f, -1f,  1f,  1f,
        -1f, -1f,  1f,  1f,
         1f,  1f,  1f, -1f,
         1f, -1f, -1f, -1f,
        -1f, -1f,  1f,  1f,
        -1f, -1f,  1f, -1f,
        -1f, -1f, -1f, -1f,
        -1f,  1f, -1f,  1f,
        -1f, -1f,  1f,  1f,
        -1f, -1f, -1f,  1f,
        -1f, -1f, -1f, -1f,
         1f,  1f, -1f,  1f,
        -1f,  1f,  1f,  1f,
         1f,  1f, -1f,  1f,
        -1f,  1f,  1f, -1f
    )

    private val cubeTexCoordsStrip = floatArrayOf(
        0f,  1f,
        1f,  1f,
        0f,  0f,
        1f,  0f,

        0f,  1f,
        1f,  1f,
        0f,  0f,
        1f,  0f,

        0f,  1f,
        1f,  1f,
        0f,  0f,
        1f,  0f,

        0f,  1f,
        1f,  1f,
        0f,  0f,
        1f,  0f,

        0f,  1f,
        1f,  1f,
        0f,  0f,
        1f,  0f,

        0f,  1f,
        1f,  1f,
        0f,  0f,
        1f,  0f,
    )

    private var mProgram = 0
    private var mTextureID : IntArray = IntArray(12)
    private val mTexturePNG = arrayOf(  //le 11 skin che dovrò mettere
        R.drawable.nzero,   //nero 0
        R.drawable.nuno,    //1
        R.drawable.ndue,    //2
        R.drawable.ntre,    //3
        R.drawable.nquattro,//4
        R.drawable.ncinque, //5
        R.drawable.nsei,    //6
        R.drawable.nsette,  //7
        R.drawable.notto,   //8
        R.drawable.vuoto,   //bianco 9
        R.drawable.bomba,   //bomba 10
        R.drawable.flag     //flag
    )

    private var muMVPMatrixHandle = 0
    private var maPositionHandle = 0
    private var maTextureHandle = 0
    private const val FLOAT_SIZE_BYTES : Int = 4
    private const val TAG = "GLES20TriangleRenderer"

    private lateinit var mTriangleVertices: FloatBuffer
    private lateinit var mTriangleTexcoords: FloatBuffer

    private const val mVertexShader =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 aPosition;" +
        "attribute vec2 aTextureCoord;" +
        "varying vec2 vTextureCoord;" +
        "void main() {" +
        "gl_Position = uMVPMatrix * aPosition;" +
        "vTextureCoord = aTextureCoord;" +
        "}"

    private const val mFragmentShader =
        "precision mediump float;" +
        "varying vec2 vTextureCoord;" +
        "uniform sampler2D sTexture;" +
        "void main() {" +
        "gl_FragColor = texture2D(sTexture, vTextureCoord);" +
        "}"

    init{
        System.loadLibrary("native-lib")
    }

    /**
     * Crea il programma di rendering
     */
    private fun createProgram(): Int {
        mTriangleTexcoords = ByteBuffer.allocateDirect(cubeTexCoordsStrip.size * FLOAT_SIZE_BYTES)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTriangleTexcoords.put(cubeTexCoordsStrip).position(0)

        mTriangleVertices = ByteBuffer.allocateDirect(cubeVerticesStrip.size * FLOAT_SIZE_BYTES)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTriangleVertices.put(cubeVerticesStrip).position(0)


        //val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexShader)
        val vertexShader = loadShader(GLES32.GL_VERTEX_SHADER, mVertexShader)
        if (vertexShader == 0) {
            return 0
        }
        //val pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShader)
        val pixelShader = loadShader(GLES32.GL_FRAGMENT_SHADER, mFragmentShader)
        if (pixelShader == 0) {
            return 0
        }
        //var program = GLES20.glCreateProgram()
        var program = GLES32.glCreateProgram()
        if (program != 0) {
            //GLES20.glAttachShader(program, vertexShader)
            GLES32.glAttachShader(program, vertexShader)
            checkGlError("glAttachShader")

            //GLES20.glAttachShader(program, pixelShader)
            GLES32.glAttachShader(program, pixelShader)
            checkGlError("glAttachShader")

            //GLES20.glLinkProgram(program)
            GLES32.glLinkProgram(program)

            val linkStatus = IntArray(1)
            //GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            GLES32.glGetProgramiv(program, GLES32.GL_LINK_STATUS, linkStatus, 0)
            //if (linkStatus[0] != GLES20.GL_TRUE) {
            if (linkStatus[0] != GLES32.GL_TRUE) {
                Log.e(TAG, "Could not link program: ")
                Log.e(TAG, GLES32.glGetProgramInfoLog(program))
                GLES32.glDeleteProgram(program)
                program = 0
            }
        }
        mProgram = program
        return program
    }

    /**
     * Carica/Compila il codice per il render
     */
    private fun loadShader(shaderType: Int, source: String): Int {
        //var shader = GLES20.glCreateShader(shaderType)
        var shader = GLES32.glCreateShader(shaderType)
        if (shader != 0) {
            //GLES20.glShaderSource(shader, source)
            //GLES20.glCompileShader(shader)

            GLES32.glShaderSource(shader, source)
            GLES32.glCompileShader(shader)


            val compiled = IntArray(1)
            //GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, compiled, 0)

            if (compiled[0] == 0) {
                Log.e(TAG, "Impossibile compilare la shader $shaderType:")
                //Log.e(TAG, GLES20.glGetShaderInfoLog(shader))
                Log.e(TAG, GLES32.glGetShaderInfoLog(shader))
                //GLES20.glDeleteShader(shader)
                GLES32.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }

    /**
     * Controlla gli errori, e lancia una eccezione in caso
     */
    private fun checkGlError(op: String) {
        var error: Int
        //while (GLES20.glGetError().also { error = it } != GLES20.GL_NO_ERROR) {
        while (GLES32.glGetError().also { error = it } != GLES32.GL_NO_ERROR) {
            Log.e(TAG, "$op: glError $error")
            throw RuntimeException("$op: glError $error")
        }
    }

    /**
     * Funzione da chiamare quando viene creata la SurfaceView
     * Tale funzione serve per inizializzare il GLES20, creare il programmaGL
     * caricare le texture da usare in game.
     */
    fun createSurfaceView(context : Context, red : Float, green : Float, blue : Float, alpha : Float){
        //GLES20.glClearColor(red, green, blue, alpha)
        //GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
        //GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        //GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        //GLES20.glDepthMask(true)

        GLES32.glClearColor(red, green, blue, alpha)
        GLES32.glClear(GLES32.GL_DEPTH_BUFFER_BIT or GLES32.GL_COLOR_BUFFER_BIT)
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)

        //aggiunta di ottimizzazione
        GLES32.glEnable(GLES32.GL_CULL_FACE)

        GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        GLES32.glDepthMask(true)

        mProgram = createProgram()
        if (mProgram == 0) {
            return
        }

        //maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")
        maPositionHandle = GLES32.glGetAttribLocation(mProgram, "aPosition")
        checkGlError("glGetAttribLocation aPosition")
        if(maPositionHandle == -1){
            throw RuntimeException("Impossibile ottenere la posizione dell'attributo per aPosition")
        }

        //maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord")
        maTextureHandle = GLES32.glGetAttribLocation(mProgram, "aTextureCoord")
        checkGlError("glGetAttribLocation aTextureCoord")
        if(maTextureHandle == -1){
            throw RuntimeException("Impossibile ottenere la posizione dell'attributo per aTextureCoord")
        }

        //muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        muMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix")
        checkGlError("glGetUniformLocation uMVPMatrix")
        if(muMVPMatrixHandle == -1){
            throw RuntimeException("Impossibile ottenere la posizione dell'attributo per uMVPMatrix")
        }

        // Creo le texture.
        var indice = 0
        for(id in mTexturePNG){
            val textures = IntArray(1)
            //GLES20.glGenTextures(1, textures, 0)
            GLES32.glGenTextures(1, textures, 0)
            mTextureID[indice] = textures[0]
            //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID[indice])
            //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat())
            //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
            //GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
            //GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, mTextureID[indice])
            GLES32.glTexParameterf(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_NEAREST.toFloat())
            GLES32.glTexParameterf(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR.toFloat())
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_REPEAT)
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_REPEAT)

            indice++

            //leggi la texture da caricare
            val ins: InputStream = context.resources.openRawResource(id)

            val bitmap: Bitmap = try{
                BitmapFactory.decodeStream(ins)
            }finally{
                try{
                    ins.close()
                }catch(e: IOException){
                    // Ignore.
                    Log.e("Texture", "Errore nella chiusura dell'inputStream")
                }
            }
            //GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
        }
    }


    /**
     * Disegna il Cubo come descritto dalla matrice passata
     */
    fun draw(mMVPMatrix : FloatArray, idTexture : Int){
        //GLES20.glUseProgram(mProgram)
        GLES32.glUseProgram(mProgram)

        //Controllo errori
        //checkGlError("glUseProgram")

        //attiva teture
        //GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID[idTexture])

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, mTextureID[idTexture])

        mTriangleVertices.position(0)
        //GLES20.glVertexAttribPointer(maPositionHandle,3, GLES20.GL_FLOAT,false,0, mTriangleVertices)
        //GLES20.glEnableVertexAttribArray(maPositionHandle)
        GLES32.glVertexAttribPointer(maPositionHandle,3, GLES32.GL_FLOAT,false,0, mTriangleVertices)
        GLES32.glEnableVertexAttribArray(maPositionHandle)

        mTriangleTexcoords.position(0)
        //GLES20.glVertexAttribPointer(maTextureHandle,2, GLES20.GL_FLOAT,false,0,mTriangleTexcoords)
        //GLES20.glEnableVertexAttribArray(maTextureHandle)
        GLES32.glVertexAttribPointer(maTextureHandle,2, GLES32.GL_FLOAT,false,0,mTriangleTexcoords)
        GLES32.glEnableVertexAttribArray(maTextureHandle)

        //disegna il cubo
        //GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0)
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4, 4)
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 8, 4)
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 12, 4)
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 16, 4)
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 20, 4)

        GLES32.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 0, 4)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 4, 4)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 8, 4)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 12, 4)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 16, 4)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 20, 4)

        //controllo errori
        //checkGlError("glDrawArrays")
    }

    fun drawJNIcall(mMVPMatrix : FloatArray, idTexture : Int){
        drawJNI(mMVPMatrix, idTexture, mProgram, mTextureID, muMVPMatrixHandle)
    }

    private external fun drawJNI(mMVPMatrix : FloatArray, idTexture : Int, program : Int, textures : IntArray, muMVPMatrixHandle : Int)
}