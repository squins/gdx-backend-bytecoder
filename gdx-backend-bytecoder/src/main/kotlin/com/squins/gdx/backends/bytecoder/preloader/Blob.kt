package com.squins.gdx.backends.bytecoder.preloader

import de.mirkosertic.bytecoder.api.web.Int8Array
import java.io.IOException
import java.io.InputStream


class Blob(val data: Int8Array) {
    fun length(): Int {
        return data.byteArrayLength()
    }

    operator fun get(i: Int): Byte {
        return data.getByte(i)
    }
}