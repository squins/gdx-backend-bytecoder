package com.squins.gdx.backends.bytecoder.files

import com.badlogic.gdx.Files
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.GdxRuntimeException
import com.squins.gdx.backends.bytecoder.makeAndLogIllegalArgumentException
import com.squins.gdx.backends.bytecoder.preloader.Preloader

class BytecoderFiles(var preloader: Preloader) : Files {
    val TAG = "BytecoderFiles"

    override fun local(path: String): FileHandle {
        throw makeAndLogIllegalArgumentException(TAG, "local() not supported in Bytecoder backend")
    }

    override fun external(path: String): FileHandle {
        throw makeAndLogIllegalArgumentException(TAG, "external() not supported in Bytecoder backend")
    }

    override fun getFileHandle(path: String, type: Files.FileType?): FileHandle {
        if (type !== Files.FileType.Internal) throw GdxRuntimeException("FileType '" + type.toString() + "' not supported in Bytecoder backend")
        return BytecoderFileHandle(preloader, path, type)
    }

    override fun absolute(path: String): FileHandle {
        throw makeAndLogIllegalArgumentException(TAG, "absolute() not supported in Bytecoder backend")
    }

    override fun classpath(path: String): FileHandle {
        return BytecoderFileHandle(preloader, path, Files.FileType.Classpath)
    }

    override fun internal(path: String): FileHandle {
        // DISABLED: performance println("BytecoderFiles internal called with path: $path")
        return BytecoderFileHandle(preloader, path, Files.FileType.Internal)
    }

    override fun getExternalStoragePath(): String? {
        return null
    }

    override fun isExternalStorageAvailable(): Boolean {
        return false
    }

    override fun getLocalStoragePath(): String? {
        return null
    }

    override fun isLocalStorageAvailable(): Boolean {
        return false
    }
}