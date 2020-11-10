package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.LongMap
import com.squins.gdx.backends.bytecoder.api.web.HtmlAudioElement

class BytecoderSound(private val delegate: HtmlAudioElement) : Sound {
    private val instances: LongMap<BytecoderMusic> = LongMap()
    private var nextId: Int = 0

    override fun pause() {
        delegate.pause()
    }

    override fun pause(soundId: Long) {
        for(music in instances.values()){
            music.pause()
        }
    }

    override fun setPitch(soundId: Long, pitch: Float) {
        TODO("Not yet implemented")
    }

    override fun setPan(soundId: Long, pan: Float, volume: Float) {
        val music: BytecoderMusic? = instances.get(soundId)
        music?.setPan(pan, volume)
    }

    override fun setLooping(soundId: Long, looping: Boolean) {
        val music: BytecoderMusic? = instances.get(soundId)
        music?.isLooping = looping
    }

    override fun play(): Long {
        return play(1f, 0f, 0f)
    }

    override fun play(volume: Float): Long {
        return play(volume, 0f, 0f)
    }

    override fun play(volume: Float, pitch: Float, pan: Float): Long {
        val id = nextId++
        val instance = BytecoderMusic(delegate)
        instance.volume = volume
        instance.setPan(pan, volume)
        instance.setOnCompletionListener {
            instances.remove(id.toLong())
            instance.dispose()
        }
        instances.put(id.toLong(), instance)
        instance.play()
        return id.toLong()
    }

    override fun stop() {
        for(music in instances.values()){
            delegate.dispose()
        }
        instances.clear()
    }

    override fun stop(soundId: Long) {
        instances.get(soundId)?.stop()
    }

    override fun setVolume(soundId: Long, volume: Float) {
        val music: BytecoderMusic? = instances.get(soundId)
        if(music != null){
            music.volume = volume
        }
    }

    override fun resume() {
        for(music in instances.values()){
            music.play()
        }
    }

    override fun resume(soundId: Long) {
        instances.get(soundId)?.play()
    }

    override fun loop(): Long {
        TODO("Not yet implemented")
    }

    override fun loop(volume: Float): Long {
        return play(volume)
    }

    override fun loop(volume: Float, pitch: Float, pan: Float): Long {
        return play(volume, pitch, pan)
    }

    override fun dispose() {
        stop()
    }
}