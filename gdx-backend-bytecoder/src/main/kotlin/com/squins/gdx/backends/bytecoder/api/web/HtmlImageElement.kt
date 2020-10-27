package com.squins.gdx.backends.bytecoder.api.web

import de.mirkosertic.bytecoder.api.web.HTMLElement

interface ImageElement : HTMLElement  {
    fun alt()
    fun height() : Int
    fun width() : Int
    fun src()
    fun isMap()
    fun setAttribute(crossOrigin: String, origin: String)
}