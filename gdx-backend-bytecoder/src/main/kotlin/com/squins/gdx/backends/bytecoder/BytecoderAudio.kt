package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.Audio
import com.badlogic.gdx.audio.AudioDevice
import com.badlogic.gdx.audio.AudioRecorder
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.files.FileHandle
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas

class BytecoderAudio(val libgdxAppCanvas: LibgdxAppCanvas) : Audio {

    override fun newAudioDevice(samplingRate: Int, isMono: Boolean): AudioDevice {
        TODO("Not yet implemented")
    }

    override fun newAudioRecorder(samplingRate: Int, isMono: Boolean): AudioRecorder {
        TODO("Not yet implemented")
    }

    override fun newMusic(file: FileHandle): Music {
        println("new music Bytecoderaudio, file: ${file.name()}")
        return BytecoderMusic(libgdxAppCanvas.audio(file.name()))
    }

    override fun newSound(fileHandle: FileHandle?): Sound {
        TODO("Not yet implemented")
    }
}