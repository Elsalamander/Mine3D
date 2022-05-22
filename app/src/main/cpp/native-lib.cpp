//
// Created by Edi on 15/05/2022.
//

#include <jni.h>
#include <GLES3/gl32.h>
#include <GLES3/gl3ext.h>

float cubeVerticesStrip[] = {
        -1.0f, -1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, -1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, -1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f, -1.0f
};

int maPositionHandle;
int maTextureHandle;

float cubeTexCoordsStrip[] = {
        0.0f,  1.0f,
        1.0f,  1.0f,
        0.0f,  0.0f,
        1.0f,  0.0f,

        0.0f,  1.0f,
        1.0f,  1.0f,
        0.0f,  0.0f,
        1.0f,  0.0f,

        0.0f,  1.0f,
        1.0f,  1.0f,
        0.0f,  0.0f,
        1.0f,  0.0f,

        0.0f,  1.0f,
        1.0f,  1.0f,
        0.0f,  0.0f,
        1.0f,  0.0f,

        0.0f,  1.0f,
        1.0f,  1.0f,
        0.0f,  0.0f,
        1.0f,  0.0f,

        0.0f,  1.0f,
        1.0f,  1.0f,
        0.0f,  0.0f,
        1.0f,  0.0f
};

/**
 * La funzione viene segnalata con un probabile bug, ma non è vero.
 * Non viene neanche invocata è un tentativo
 */
extern "C"
JNIEXPORT void JNICALL
Java_it_elsalamander_mine3d_Game_Graphic_Engine_GLCube_drawJNI(JNIEnv *env,
                                   jobject thiz,
                                   jfloatArray m_mvpmatrix,
                                   jint id_texture,
                                   jint program,
                                   jintArray textures,
                                   jint muMVPMatrixHandle) {


    glUseProgram(program);

    glActiveTexture(GL_TEXTURE0);

    int text = (env->GetIntArrayElements(textures, nullptr))[id_texture];
    glBindTexture(GL_TEXTURE_2D, text);

    glVertexAttribPointer(maPositionHandle,3, GL_FLOAT,false,0, cubeVerticesStrip);
    glEnableVertexAttribArray(maPositionHandle);

    glVertexAttribPointer(maTextureHandle,2, GL_FLOAT,false,0,cubeTexCoordsStrip);
    glEnableVertexAttribArray(maTextureHandle);

    //disegna il cubo
    glUniformMatrix4fv(muMVPMatrixHandle, 1, false, reinterpret_cast<const float*>(m_mvpmatrix));
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    glDrawArrays(GL_TRIANGLE_STRIP, 4, 4);
    glDrawArrays(GL_TRIANGLE_STRIP, 8, 4);
    glDrawArrays(GL_TRIANGLE_STRIP, 12, 4);
    glDrawArrays(GL_TRIANGLE_STRIP, 16, 4);
    glDrawArrays(GL_TRIANGLE_STRIP, 20, 4);

}