package main.examples

import java.nio.FloatBuffer

// TODO: what is the correct location? Also used by bytecoder backend?
object FloatConversion {
    fun convertBytecoderFloatToFloatBuffer(source : de.mirkosertic.bytecoder.api.web.FloatArray): FloatBuffer {
        // DISABLED: performance println("convertBytecoderFloatToFloatBuffer length: ${source.floatArrayLength()}")
        val buffer = FloatBuffer.allocate(source.floatArrayLength())
        // DISABLED: performance println("buffer length: ${buffer.capacity()}")
        for(i in 0 until source.floatArrayLength()) {
            // DISABLED: performance println("get at index: $i source:  ${source.getFloat(i)}")
            buffer.put(source.getFloat(i))
            // DISABLED: performance println("loop round: $i - last buffer ${buffer.position()}")
        }
        // DISABLED: performance println("after for loop")
        // DISABLED: performance println("Buffer output: ${buffer.get(5)}")
        return buffer
    }

}