package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.audio.Music
import de.mirkosertic.bytecoder.api.web.Event
import de.mirkosertic.bytecoder.api.web.EventListener
import com.squins.gdx.backends.bytecoder.api.web.HtmlAudioElement

class BytecoderMusic(private val delegate: HtmlAudioElement) : Music {
    override fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLooping(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setOnCompletionListener(listener: Music.OnCompletionListener) {
        delegate.addEventListener("Ended", EventListener <Event> {
            listener.onCompletion(this)
        })
    }

    override fun pause() {
        delegate.pause()
    }

    override fun setPan(pan: Float, volume: Float) {
        TODO("Not yet implemented")
    }

    override fun getPosition(): Float {
        TODO("Not yet implemented")
    }

    override fun setLooping(isLooping: Boolean) {
        delegate.setLooping(isLooping)
    }

    override fun getVolume(): Float {
        TODO("Not yet implemented")
    }

    override fun play() {
        delegate.play()
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun setVolume(volume: Float) {
        delegate.setVolume(volume)
    }

    override fun setPosition(position: Float) {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        delegate.dispose()
    }
}