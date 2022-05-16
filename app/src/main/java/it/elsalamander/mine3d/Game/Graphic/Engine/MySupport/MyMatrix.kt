package it.elsalamander.mine3d.Game.Graphic.Engine.MySupport

class MyMatrix {

    companion object{
        fun scaleM(m: FloatArray, zoom: Float) {
            m[0] *= zoom
            m[4] *= zoom
            m[8] *= zoom

            m[1] *= zoom
            m[5] *= zoom
            m[9] *= zoom

            m[2] *= zoom
            m[6] *= zoom
            m[10] *= zoom

            m[3] *= zoom
            m[7] *= zoom
            m[11] *= zoom
        }

        fun translateM(m: FloatArray, x: Float, y: Float, z: Float) {
            m[12] += m[0] * x + m[4] * y + m[8] * z
            m[13] += m[1] * x + m[5] * y + m[9] * z
            m[14] += m[2] * x + m[6] * y + m[10] * z
            m[15] += m[3] * x + m[7] * y + m[11] * z
        }

        fun arraycopy(src : FloatArray, dest : FloatArray){
            dest[0] = src[0]
            dest[1] = src[1]
            dest[2] = src[2]
            dest[3] = src[3]
            dest[4] = src[4]
            dest[5] = src[5]
            dest[6] = src[6]
            dest[7] = src[7]
            dest[8] = src[8]
            dest[9] = src[9]
            dest[10] = src[10]
            dest[11] = src[11]
            dest[12] = src[12]
            dest[13] = src[13]
            dest[14] = src[14]
            dest[15] = src[15]
        }
    }
}