package com.squins.gdx.backends.bytecoder.api.web

import com.squins.gdx.backends.bytecoder.api.web.webgl.WebMat4

interface LibgdxAppCanvas: HTMLWebGLCanvasElement {
    fun sound(name: String): HtmlAudioElement
    fun audio(name:String): HtmlAudioElement
    fun mat4() : WebMat4
    fun assetBaseUrl():String
}