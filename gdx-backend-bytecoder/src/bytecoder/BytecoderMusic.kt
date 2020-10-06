package com.libgdx.backends.bytecoder

import com.badlogic.gdx.audio.Music
import ext.WebAudio

class BytecoderMusic(private val delegate: WebAudio) : Music {
    override fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLooping(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setOnCompletionListener(listener: Music.OnCompletionListener?) {
        delegate.setOnCompletionListener(listener)
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