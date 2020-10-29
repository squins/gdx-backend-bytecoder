package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.Files
import com.badlogic.gdx.files.FileHandle

class BytecoderFiles() : Files {
    override fun getLocalStoragePath(): String {
        TODO("Not yet implemented")
    }

    override fun local(path: String?): FileHandle {
        TODO("Not yet implemented")
    }

    override fun external(path: String?): FileHandle {
        TODO("Not yet implemented")
    }

    override fun getFileHandle(path: String?, type: Files.FileType?): FileHandle {
        TODO("Not yet implemented")
    }

    override fun absolute(path: String?): FileHandle {
        TODO("Not yet implemented")
    }

    override fun isExternalStorageAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun classpath(path: String?): FileHandle {
        TODO("Not yet implemented")
    }

    override fun internal(path: String): FileHandle {
        println("BytecoderFiles internal called with path: $path")
        return FileHandle(path)
    }

    override fun getExternalStoragePath(): String {
        TODO("Not yet implemented")
    }

    override fun isLocalStorageAvailable(): Boolean {
        TODO("Not yet implemented")
    }
}