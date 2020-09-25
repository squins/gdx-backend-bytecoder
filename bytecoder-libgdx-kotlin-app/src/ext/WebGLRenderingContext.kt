package ext

import de.mirkosertic.bytecoder.api.OpaqueReferenceType


interface WebGLRenderingContext : OpaqueReferenceType {

    fun clear(mask: Int)

    fun clearColor(red: Float, blue: Float, green: Float, alpha: Float)
}

