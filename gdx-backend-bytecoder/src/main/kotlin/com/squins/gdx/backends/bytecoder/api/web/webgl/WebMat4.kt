package com.squins.gdx.backends.bytecoder.api.web.webgl

import de.mirkosertic.bytecoder.api.OpaqueReferenceType
import de.mirkosertic.bytecoder.api.web.FloatArray

interface WebMat4 : OpaqueReferenceType {

    fun create() : FloatArray

    fun perspective(out: FloatArray, fovy: Float, aspect: Int, near :  Float, far : Float)

    fun translate(out: FloatArray, a: FloatArray, v: FloatArray)
}