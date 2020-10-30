package com.squins.gdx.backends.bytecoder.api.web

import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.web.HTMLElement

/**
 * Represents Web Audio element
 */
interface HtmlAudioElement: HTMLElement {

    @OpaqueProperty("loop")
    fun setLooping(loop : Boolean)

    @OpaqueProperty("volume")
    fun setVolume(volume: Float)

    @OpaqueProperty("volume")
    fun getVolume() : Float

    fun play()

    fun pause()

    @OpaqueProperty("stop")
    fun dispose()
}
