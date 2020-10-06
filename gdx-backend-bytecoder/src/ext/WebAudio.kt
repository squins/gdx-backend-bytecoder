package ext

import com.badlogic.gdx.audio.Music
import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.OpaqueReferenceType

interface WebAudio: OpaqueReferenceType {

    fun setOnCompletionListener(listener: Music.OnCompletionListener?)

    @OpaqueProperty("loop")
    fun setLooping(loop : Boolean)

    @OpaqueProperty("volume")
    fun setVolume(volume: Float)

    fun play()

    fun pause()

    @OpaqueProperty("stop")
    fun dispose()
}
