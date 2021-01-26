package com.squins.gdx.backends.bytecoder.api.web

import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.web.HTMLElement

/**
 * Represents Web Div element
 */
interface HTMLDivElement : HTMLElement {

    @OpaqueProperty
    fun clientWidth(): Int

    @OpaqueProperty
    fun clientHeight(): Int

    @OpaqueProperty
    fun style(style: String)
}