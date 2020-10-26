package com.squins.gdx.backends.bytecoder.api.web

import de.mirkosertic.bytecoder.api.web.Element

interface ImageElement : Element  {
    fun alt()
    fun height() : Int
    fun width() : Int
    fun src()
    fun isMap()

}