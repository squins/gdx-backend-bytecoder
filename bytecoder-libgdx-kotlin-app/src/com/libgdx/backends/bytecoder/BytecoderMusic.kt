package com.libgdx.backends.bytecoder

import com.badlogic.gdx.audio.Music
import ext.WebAudio

class BytecoderMusic(val delegate: WebAudio) : Music {
    override fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLooping(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setOnCompletionListener(listener: Music.OnCompletionListener?) {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun setPan(pan: Float, volume: Float) {
        TODO("Not yet implemented")
    }

    override fun getPosition(): Float {
        TODO("Not yet implemented")
    }

    override fun setLooping(isLooping: Boolean) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun setPosition(position: Float) {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}