package com.squins.gdx.backends.bytecoder.api.web

import de.mirkosertic.bytecoder.api.web.HTMLAudioElement
import de.mirkosertic.bytecoder.api.web.webgl.WebMat4

/**
 * Represents LibgdxCanvas
 */
interface LibgdxAppCanvas: HTMLWebGLCanvasElement {
    fun sound(name: String): HTMLAudioElement
    fun audio(name:String): HTMLAudioElement
    fun mat4() : WebMat4
    fun assetBaseUrl():String
}