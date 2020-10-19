package com.squins.gdx.backends.bytecoder.api.web

import com.squins.gdx.backends.bytecoder.api.web.webgl.WebGLRenderingContext
import de.mirkosertic.bytecoder.api.web.HTMLElement

interface HTMLWebGLCanvasElement : HTMLElement {

    fun getContext(context: String?) : WebGLRenderingContext
}