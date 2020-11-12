package com.squins.gdx.backends.bytecoder.api.web

import de.mirkosertic.bytecoder.api.OpaqueMethod
import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.web.HTMLElement

/**
 * Represents Web Audio element
 */
interface HtmlAudioElement: HTMLElement {

    @OpaqueProperty("loop")
    fun setLooping(loop : Boolean)

    @OpaqueProperty("loop")
    fun isLoop() : Boolean

    @OpaqueProperty("volume")
    fun setVolume(volume: Float)

    @OpaqueProperty("volume")
    fun getVolume() : Float

    @OpaqueProperty("currentTime")
    fun setCurrentTime(currentTime: Int)

    @OpaqueProperty("currentTime")
    fun getCurrentTime() : Int

    @OpaqueProperty("paused")
    fun isPaused() : Boolean

    @OpaqueProperty("ended")
    fun isEnded() : Boolean

    fun play()

    fun pause()

    @OpaqueMethod("stop")
    fun dispose()

    @OpaqueProperty("src")
    fun setSrc(url: String)

}
