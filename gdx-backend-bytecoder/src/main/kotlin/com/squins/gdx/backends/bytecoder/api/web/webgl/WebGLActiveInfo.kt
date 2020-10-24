package com.squins.gdx.backends.bytecoder.api.web.webgl

import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.OpaqueReferenceType

abstract class WebGLActiveInfo : OpaqueReferenceType {
    @OpaqueProperty("size")
    abstract fun getSize(): Int

    @OpaqueProperty("name")
    abstract fun getName(): String

    @OpaqueProperty("type")
    abstract fun getType(): Int
}