package com.squins.gdx.backends.bytecoder.preloader

import de.mirkosertic.bytecoder.api.web.Int8Array
import java.io.IOException
import java.io.InputStream
import kotlin.experimental.and


class Blob(val data: Int8Array) {
    fun length(): Int {
        return data.byteArrayLength()
    }

    operator fun get(i: Int): Byte {
        return data.getByte(i)
    }

//    fun read(): InputStream {
//        return object : InputStream() {
//            @Throws(IOException::class)
//            override fun read(): Int {
//                return if (pos == length()) -1 else get(pos++) && 0xff
//            }
//
//            override fun available(): Int {
//                return length() - pos
//            }
//
//            var pos = 0
//        }
//    }

//    fun toBase64(): String {
//        val length: Int = data.byteArrayLength()
//        val base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
//        val encoded = StringBuilder(length * 4 / 3 + 2)
//        var i = 0
//        while (i < length) {
//            if (length - i >= 3) {
//                val j: Int = (data.getByte(i) && 0xff shl 16) + (data.getByte(i + 1) && 0xff shl 8) + (data.getByte(i + 2) and 0xff)
//                encoded.append(base64code[j shr 18 and 0x3f])
//                encoded.append(base64code[j shr 12 and 0x3f])
//                encoded.append(base64code[j shr 6 and 0x3f])
//                encoded.append(base64code[j and 0x3f])
//            } else if (length - i >= 2) {
//                val j: Int = (data.get(i) and 0xff shl 16) + (data.get(i + 1) and 0xff shl 8)
//                encoded.append(base64code[j shr 18 and 0x3f])
//                encoded.append(base64code[j shr 12 and 0x3f])
//                encoded.append(base64code[j shr 6 and 0x3f])
//                encoded.append("=")
//            } else {
//                val j: Int = data.get(i) and 0xff shl 16
//                encoded.append(base64code[j shr 18 and 0x3f])
//                encoded.append(base64code[j shr 12 and 0x3f])
//                encoded.append("==")
//            }
//            i += 3
//        }
//        return encoded.toString()
//    }

}