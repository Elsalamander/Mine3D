package it.elsalamander.mine3d.Game.Graphic.Engine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import it.elsalamander.mine3d.R
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

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
        0f,  0f,  1f,  0f,
        0f,  1f,  1f,  1f,
        0f,  0f,  1f,  0f,
        0f,  1f,  1f,  1f,
        0f,  0f,  1f,  0f,
        0f,  1f,  1f,  1f,
        0f,  0f,  1f,  0f,
        0f,  1f,  1f,  1f,
        0f,  0f,  1f,  0f,
        0f,  1f,  1f,  1f,
        0f,  0f,  1f,  0f,
        0f,  1f,  1f,  1f
    )

    private var mProgram = 0
    private var mTextureID = 0
    private var muMVPMatrixHandle = 0
    private var maPositionHandle = 0
    private var maTextureHandle = 0
    private const val FLOAT_SIZE_BYTES : Int = 4
    private const val TAG = "GLES20TriangleRenderer"

    private lateinit var mTriangleVertices: FloatBuffer
    private lateinit var mTriangleTexcoords: FloatBuffer

    private val mVertexShader =
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 aPosition;" +
                "attribute vec2 aTextureCoord;" +
                "varying vec2 vTextureCoord;" +
                "void main() {" +
                "gl_Position = uMVPMatrix * aPosition;" +
                "vTextureCoord = aTextureCoord;" +
                "}"

    private val mFragmentShader =
        "precision mediump float;" +
                "varying vec2 vTextureCoord;" +
                "uniform sampler2D sTexture;" +
                "void main() {" +
                "gl_FragColor = texture2D(sTexture, vTextureCoord);" +
                "}"


    /**
     * Crea il programma di rendering
     */
    fun createProgram(): Int {
        mTriangleTexcoords = ByteBuffer.allocateDirect(cubeTexCoordsStrip.size * FLOAT_SIZE_BYTES)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTriangleTexcoords.put(cubeTexCoordsStrip).position(0)

        mTriangleVertices = ByteBuffer.allocateDirect(cubeVerticesStrip.size * FLOAT_SIZE_BYTES)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTriangleVertices.put(cubeVerticesStrip).position(0)


        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexShader)
        if (vertexShader == 0) {
            return 0
        }
        val pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShader)
        if (pixelShader == 0) {
            return 0
        }
        var program = GLES20.glCreateProgram()
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader)
            checkGlError("glAttachShader")
            GLES20.glAttachShader(program, pixelShader)
            checkGlError("glAttachShader")
            GLES20.glLinkProgram(program)
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ")
                Log.e(TAG, GLES20.glGetProgramInfoLog(program))
                GLES20.glDeleteProgram(program)
                program = 0
            }
        }
        mProgram = program
        return program
    }

    /**
     * Carica/Compila il codice per il render
     */
    fun loadShader(shaderType: Int, source: String): Int {
        var shader = GLES20.glCreateShader(shaderType)
        if (shader != 0) {
            GLES20.glShaderSource(shader, source)
            GLES20.glCompileShader(shader)
            val compiled = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader $shaderType:")
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader))
                GLES20.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }

    /**
     * Controlla gli errori
     */
    fun checkGlError(op: String) {
        var error: Int
        while (GLES20.glGetError().also { error = it } != GLES20.GL_NO_ERROR) {
            Log.e(TAG, "$op: glError $error")
            //throw RuntimeException("$op: glError $error")
        }
    }

    @SuppressLint("ResourceType")
    fun createSurfaceView(context : Context){
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glDepthMask(true)

        mProgram = createProgram()
        if (mProgram == 0) {
            return
        }

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")
        checkGlError("glGetAttribLocation aPosition")
        if(maPositionHandle == -1){
            throw RuntimeException("Could not get attrib location for aPosition")
        }

        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord")
        checkGlError("glGetAttribLocation aTextureCoord")
        if(maTextureHandle == -1){
            throw RuntimeException("Could not get attrib location for aTextureCoord")
        }

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        checkGlError("glGetUniformLocation uMVPMatrix")
        if(muMVPMatrixHandle == -1){
            throw RuntimeException("Could not get attrib location for uMVPMatrix")
        }

        // Creo la texture.
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        mTextureID = textures[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID)
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)

        //leggi la texture da caricare
        val ins: InputStream = context.resources.openRawResource(R.drawable.nuno)

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
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()
    }


    /**
     * Disegna il Cubo come descritto dalla matrice passata
     */
    fun draw(mMVPMatrix : FloatArray){
        //GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f)
        //GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
        //GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        //GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        //GLES20.glDepthMask(true)
        GLES20.glUseProgram(mProgram)

        //Controllo errori
        checkGlError("glUseProgram")

        //attiva teture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID)

        mTriangleVertices.position(0)
        GLES20.glVertexAttribPointer(maPositionHandle,3, GLES20.GL_FLOAT,false,0, mTriangleVertices)
        GLES20.glEnableVertexAttribArray(maPositionHandle)

        mTriangleTexcoords.position(0)
        GLES20.glVertexAttribPointer(maTextureHandle,2, GLES20.GL_FLOAT,false,0,mTriangleTexcoords)
        GLES20.glEnableVertexAttribArray(maTextureHandle)

        //disegna il cubo
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4, 4)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 8, 4)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 12, 4)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 16, 4)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 20, 4)

        //controllo errori
        checkGlError("glDrawArrays")
    }

}