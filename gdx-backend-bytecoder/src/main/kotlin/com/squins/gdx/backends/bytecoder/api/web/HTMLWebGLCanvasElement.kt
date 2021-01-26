package com.squins.gdx.backends.bytecoder.api.web

import com.squins.gdx.backends.bytecoder.api.web.webgl.WebGLRenderingContext
import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.web.HTMLElement

/**
 * Represents Web Canvas element
 */
interface HTMLWebGLCanvasElement : HTMLElement {

    @OpaqueProperty
    fun height(): Int
    @OpaqueProperty
    fun width(): Int

    fun getContext(context: String?) : WebGLRenderingContext
}
