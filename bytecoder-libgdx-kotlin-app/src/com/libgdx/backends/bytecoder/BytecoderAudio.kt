package com.libgdx.backends.bytecoder

import com.badlogic.gdx.Audio
import com.badlogic.gdx.audio.AudioDevice
import com.badlogic.gdx.audio.AudioRecorder
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.files.FileHandle
import ext.LibgdxAppCanvas
import ext.WebAudio

class BytecoderAudio(val libgdxAppCanvas: LibgdxAppCanvas) : Audio {

    override fun newAudioDevice(samplingRate: Int, isMono: Boolean): AudioDevice {
        TODO("Not yet implemented")
    }

    override fun newAudioRecorder(samplingRate: Int, isMono: Boolean): AudioRecorder {
        TODO("Not yet implemented")
    }

    override fun newMusic(file: FileHandle?): Music {
        println("new music Bytecoderaudio")
        return BytecoderMusic(libgdxAppCanvas.audio("bla.m4a"))
        TODO("Not yet implemented")
    }

    override fun newSound(fileHandle: FileHandle?): Sound {
        TODO("Not yet implemented")
    }
}