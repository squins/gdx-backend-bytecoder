package com.squins.gdx.backends.bytecoder.api.web

import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.web.HTMLElement

interface HtmlImageElement : HTMLElement  {
    fun alt()
    fun height() : Int
    fun width() : Int

    @OpaqueProperty("src")
    fun setSrc(url: String)
    fun isMap()
    fun crossOrigin(crossOrigin: String)
}