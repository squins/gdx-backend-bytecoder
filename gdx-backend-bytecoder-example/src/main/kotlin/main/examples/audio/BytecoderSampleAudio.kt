package main.examples.audio

import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas

class BytecoderSampleAudio(canvas: LibgdxAppCanvas) {
    private val audio = canvas.audio("sample.mp3")
    fun run() {
        //audio.setLooping(true) //music keeps on playing
        audio.setVolume(0.75F)
        audio.play()
    }
}