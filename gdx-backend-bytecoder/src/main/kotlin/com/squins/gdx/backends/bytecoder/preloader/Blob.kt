/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squins.gdx.backends.bytecoder.preloader

import de.mirkosertic.bytecoder.api.web.Int8Array
import java.io.IOException
import java.io.InputStream
import kotlin.experimental.and

open class Blob(protected val data: Int8Array) {
    fun length(): Int {
        return data.byteArrayLength()
    }

    operator fun get(i: Int): Byte {
        return data.getByte(i)
    }

    fun read(): InputStream {
        return object : InputStream() {
            @Throws(IOException::class)
            override fun read(): Int {
                return if (pos == length()) -1 else (get(pos++) and 0xff.toByte()).toInt()
            }

            override fun available(): Int {
                return length() - pos
            }

            var pos = 0
        }
    }

//    fun toBase64(): String {
//        val length: Int = data.byteArrayLength()
//        val base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
//        val encoded = StringBuilder(length * 4 / 3 + 2)
//        var i = 0
//        while (i < length) {
//            if (length - i >= 3) {
//                val j: Int = (data.getByte(i) and 0xff.toByte() shl 16) + (data.getByte(i + 1) and 0xff.toByte() shl 8) + (data.getByte(i + 2) and 0xff.toByte())
//                encoded.append(base64code[j shr 18 and 0x3f])
//                encoded.append(base64code[j shr 12 and 0x3f])
//                encoded.append(base64code[j shr 6 and 0x3f])
//                encoded.append(base64code[j and 0x3f])
//            } else if (length - i >= 2) {
//                val j: Int = (data.getByte(i) and 0xff.toByte() shl 16) + (data.getByte(i + 1) and 0xff.toByte() shl 8)
//                encoded.append(base64code[j shr 18 and 0x3f])
//                encoded.append(base64code[j shr 12 and 0x3f])
//                encoded.append(base64code[j shr 6 and 0x3f])
//                encoded.append("=")
//            } else {
//                val j: Int = data.getByte(i) and 0xff.toByte() shl 16
//                encoded.append(base64code[j shr 18 and 0x3f])
//                encoded.append(base64code[j shr 12 and 0x3f])
//                encoded.append("==")
//            }
//            i += 3
//        }
//        return encoded.toString()
//    }
}