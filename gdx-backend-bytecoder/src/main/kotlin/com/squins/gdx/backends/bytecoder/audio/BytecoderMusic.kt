package com.squins.gdx.backends.bytecoder.audio

import com.badlogic.gdx.audio.Music
import de.mirkosertic.bytecoder.api.web.EventListener
import de.mirkosertic.bytecoder.api.web.HTMLAudioElement

class BytecoderMusic(private val delegate: HTMLAudioElement) : Music {
    private var started: Boolean = false

    override fun isPlaying(): Boolean {
        return started && !delegate.isPaused() && delegate.isEnded()
    }

    override fun isLooping(): Boolean {
        return delegate.isLoop()
    }

    override fun setOnCompletionListener(listener: Music.OnCompletionListener) {
        delegate.addEventListener("Ended", EventListener {
            listener.onCompletion(this)
        })
    }

    override fun pause() {
        delegate.pause()
    }

    override fun setPan(pan: Float, volume: Float) {
        delegate.setVolume(volume)
    }

    override fun getPosition(): Float {
        return delegate.getCurrentTime().toFloat()
    }

    override fun setLooping(isLooping: Boolean) {
        delegate.setLoop(isLooping)
    }

    override fun getVolume(): Float {
        return delegate.getVolume()
    }

    override fun play() {
        delegate.play()
        started = true
    }

    override fun stop() {
       delegate.pause()
       delegate.setCurrentTime(0)
        started = false
    }

    override fun setVolume(volume: Float) {
        delegate.setVolume(volume)
    }

    override fun setPosition(position: Float) {
        delegate.setCurrentTime(position.toInt())
    }

    override fun dispose() {
        delegate.stop()
    }
}