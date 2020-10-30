package com.squins.gdx.backends.bytecoder.api.web

import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.web.HTMLElement

/**
 * Represents Web Image element
 */
interface HtmlImageElement : HTMLElement  {
    @OpaqueProperty("alt")
    fun getAlt() : String

    @OpaqueProperty("height")
    fun getHeight() : Int

    @OpaqueProperty("width")
    fun getWidth() : Int

    @OpaqueProperty("height")
    fun setHeight(height: Int)

    @OpaqueProperty("width")
    fun setWidth(width: Int)

    @OpaqueProperty("src")
    fun setSrc(url: String)

    @OpaqueProperty("src")
    fun getSrc() : String?

    @OpaqueProperty("isMap")
    fun isMap(): Boolean

    @OpaqueProperty("crossOrigin")
    fun setCrossOrigin(crossOrigin: String)
}