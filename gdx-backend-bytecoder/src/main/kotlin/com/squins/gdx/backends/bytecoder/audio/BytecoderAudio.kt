package com.squins.gdx.backends.bytecoder.audio

import com.badlogic.gdx.Audio
import com.badlogic.gdx.audio.AudioDevice
import com.badlogic.gdx.audio.AudioRecorder
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.files.FileHandle
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import com.squins.gdx.backends.bytecoder.makeAndLogIllegalArgumentException

class BytecoderAudio(val libgdxAppCanvas: LibgdxAppCanvas) : Audio {

    override fun newAudioDevice(samplingRate: Int, isMono: Boolean): AudioDevice {
        throw makeAndLogIllegalArgumentException("BytecoderAudio", "AudioDevice not supported by Bytecoder backend")
    }

    override fun newAudioRecorder(samplingRate: Int, isMono: Boolean): AudioRecorder {
        throw makeAndLogIllegalArgumentException("BytecoderAudio", "AudioRecorder not supported by Bytecoder backend")
    }

    override fun newMusic(file: FileHandle): Music {
        // DISABLED: performance println("new music Bytecoderaudio, file: ${file.name()}")
        return BytecoderMusic(libgdxAppCanvas.audio("assets/${file.name()}"))
    }

    override fun newSound(fileHandle: FileHandle): Sound {
        // DISABLED: performance println("("new music BytecoderSound, file: ${fileHandle.name()}")
        return BytecoderSound(libgdxAppCanvas.sound(fileHandle.name()))
    }
}