package it.elsalamander.mine3d.Game.Graphic.V2

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import android.util.Log
import it.elsalamander.mine3d.Game.Graphic.GLCubeBase
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.opengles.GL10


object GLCubeBaseV2 {
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

    private var mPositionHandle = 0
    private var mColorHandle = 0

    private val mtrxModel = FloatArray(16)

    // Store the projection matrix. This is used to project the scene onto a 2D viewport.
    private val mtrxProjection = FloatArray(16)

    // Allocate storage for the final combined matrix. This will be passed into the shader program.
    private val mtrxMVP = FloatArray(16)

    // Create our UV coordinates.
    var uvArray : FloatArray = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    )
    var uvBuffer: FloatBuffer? = null
    var indices = shortArrayOf(0, 1, 2, 0, 2, 3) // The order of vertexrendering.

    var indicesBuffer: ShortBuffer? = null

    var tmp_id_texture : Int = 0

    private const val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex


    init{
        //inizliazza il vertex buffer per le coordinate
        //val bb = ByteBuffer.allocateDirect(cubeCoords.size * 4)
        //bb.order(ByteOrder.nativeOrder())
        //vertexBuffer = bb.asFloatBuffer()
        //vertexBuffer?.put(cubeCoords)
        //vertexBuffer?.position(0)

        uvBuffer = ByteBuffer.allocateDirect(uvArray.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .put(uvArray).position(0) as FloatBuffer?

        // initialize byte buffer for the draw list

        // initialize byte buffer for the draw list
        indicesBuffer = ByteBuffer.allocateDirect(indices.size * 2).order(ByteOrder.nativeOrder()).asShortBuffer()
            .put(indices).position(0) as ShortBuffer?

        val vertices = floatArrayOf(0f, 0f, 0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f, 0f, 0f)

        // The vertex buffer.

        // The vertex buffer.
        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .put(vertices).position(0) as FloatBuffer?


    }

    /**
     * Disegna il cubo con i parametri passati
     */
    fun draw(mvpMatrix: FloatArray?) {
        var mMVPMatrixHandle = 0

        GLES30.glUseProgram(ShaderHelper.programTexture)

        // Get handle to shape's transformation matrix
        // Get handle to shape's transformation matrix
        val u_MVPMatrix = GLES30.glGetUniformLocation(ShaderHelper.programTexture, "u_MVPMatrix")
        val a_Position = GLES30.glGetAttribLocation(ShaderHelper.programTexture, "a_Position")
        val a_texCoord = GLES30.glGetAttribLocation(ShaderHelper.programTexture, "a_texCoord")
        val u_texture = GLES30.glGetUniformLocation(ShaderHelper.programTexture, "u_texture")

        GLES30.glEnableVertexAttribArray(a_Position);
        GLES30.glEnableVertexAttribArray(a_texCoord);
        GLES30.glEnableVertexAttribArray(u_MVPMatrix);
        GLES30.glEnableVertexAttribArray(u_texture);


        //disegna le faccie

        for (face in 0..5) {
            GLES30.glVertexAttribPointer(a_Position, 3, GLES30.GL_FLOAT, false, 12, vertexBuffer);

            // Prepare the texturecoordinates
            GLES30.glVertexAttribPointer(a_texCoord, 3, GLES30.GL_FLOAT, false, 12, uvBuffer);

            //crea il buffer per le coordinate
            val dlb = ByteBuffer.allocateDirect(
                drawOrder[face].size * 2
            )
            dlb.order(ByteOrder.nativeOrder())
            val drawListBuffer = dlb.asShortBuffer()
            drawListBuffer.put(drawOrder[face])
            drawListBuffer.position(0)



            // Set the sampler texture unit to where we have saved the texture.
            //GLES30.glUniform1i(u_texture, tmp_id_texture);

            GLES30.glUniform1i(u_texture, 0);
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, tmp_id_texture);

            // Pass the data to shaders - end

            GLES30.glUniformMatrix4fv(u_MVPMatrix, 1, false, mvpMatrix, 0);
            // Draw the triangles
            //GLES30.glDrawElements(GLES30.GL_TRIANGLES, indices.size, GLES30.GL_UNSIGNED_SHORT, indicesBuffer);
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, drawOrder[face].size, GLES30.GL_UNSIGNED_SHORT, drawListBuffer)
        }

        //disabilita
        GLES30.glDisableVertexAttribArray(mMVPMatrixHandle)

    }

    /**
     * Crea il programma GL per poi fare il DRAW
     */
    fun createGlProgram(vertexShader : Int, fragmentShader : Int): Int {
        // create empty OpenGL ES Program
        val mProgram = GLES30.glCreateProgram()

        // add the vertex shader to program
        GLES30.glAttachShader(mProgram, vertexShader)

        // add the fragment shader to program
        GLES30.glAttachShader(mProgram, fragmentShader)

        // creates OpenGL ES program executables
        GLES30.glLinkProgram(mProgram)

        return mProgram
    }

    fun createGLSprite(assets: Context, id: Int) : Int{
        // Construct an input stream to texture image "res\drawable\crate.png"
        val istream: InputStream = assets.resources.openRawResource(id)
        val bmp: Bitmap = try {
            // Read and decode input as bitmap
            BitmapFactory.decodeStream(istream)
        } finally {
            try {
                istream.close()
            } catch (e: IOException) {
            }
        }
        val texture_id = createGlTexture()
        Log.d("G1", "image id = " + texture_id)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bmp, 0)
        bmp.recycle()

        return texture_id
    }

    private fun createGlTexture(): Int {
        // Generate Textures, if more needed, alter these numbers.
        val textureHandles = IntArray(1)
        GLES30.glGenTextures(1, textureHandles, 0)
        return if (textureHandles[0] != 0) {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandles[0])
            textureHandles[0]
        } else {
            throw RuntimeException("Error loading texture.")
        }
    }

}