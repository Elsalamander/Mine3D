package it.elsalamander.mine3d.Game.Graphic.V2

import android.opengl.GLES30


object ShaderHelper {
    const val vs_Image =
            //"#version 300 es" +
            "uniform mat4 u_MVPMatrix;" +
            "attribute vec4 a_Position;" +
            "attribute vec2 a_texCoord;" +
            "varying vec2 v_texCoord;" +
            "void main() {" +
            "  gl_Position = u_MVPMatrix * a_Position;" +
            "  v_texCoord = a_texCoord;" +
            "}"
    const val fs_Image =
            //"#version 300 es" +
            "precision mediump float;" +
            "uniform sampler2D u_texture;" +
            "varying vec2 v_texCoord;" +
            "void main() {" +
            "  gl_FragColor = texture2D(u_texture, v_texCoord);" +
            "}"

    // Program variables
    var programTexture = 0
    var vertexShaderImage = 0
    var fragmentShaderImage = 0
    fun loadShader(type: Int, shaderCode: String?): Int {

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        val shader = GLES30.glCreateShader(type)

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode)
        GLES30.glCompileShader(shader)

        // return the shader
        return shader
    }

    fun initGlProgram() {
        // Create the shaders, images
        vertexShaderImage = loadShader(GLES30.GL_VERTEX_SHADER, vs_Image)
        fragmentShaderImage = loadShader(GLES30.GL_FRAGMENT_SHADER, fs_Image)
        programTexture = GLES30.glCreateProgram() // create empty OpenGL ES Program
        GLES30.glAttachShader(programTexture, vertexShaderImage) // add the vertex shader to program
        GLES30.glAttachShader(
            programTexture,
            fragmentShaderImage
        ) // add the fragment shader to program
        GLES30.glLinkProgram(programTexture) // creates OpenGL ES program executables
    }

    fun dispose() {
        GLES30.glDetachShader(programTexture, vertexShaderImage)
        GLES30.glDetachShader(programTexture, fragmentShaderImage)
        GLES30.glDeleteShader(fragmentShaderImage)
        GLES30.glDeleteShader(vertexShaderImage)
        GLES30.glDeleteProgram(programTexture)
    }
}