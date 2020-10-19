package main.examples

import java.nio.FloatBuffer

// TODO: what is the correct location? Also used by bytecoder backend?
object FloatConversion {
    fun convertBytecoderFloatToFloatBuffer(source : de.mirkosertic.bytecoder.api.web.FloatArray): FloatBuffer {
        val buffer = FloatBuffer.allocate(source.floatArrayLength())
        for(i in 0..source.floatArrayLength()) {
            buffer.put(source.getFloat(i))
        }
        return buffer
    }

}