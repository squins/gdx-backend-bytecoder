package com.squins.gdx.backends.bytecoder.api.web

import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.web.EventTarget

/**
 * Represents Web Audio element
 */
interface WebAudio: EventTarget {

    @OpaqueProperty("loop")
    fun setLooping(loop : Boolean)

    @OpaqueProperty("volume")
    fun setVolume(volume: Float)

    fun play()

    fun pause()

    @OpaqueProperty("stop")
    fun dispose()
}
