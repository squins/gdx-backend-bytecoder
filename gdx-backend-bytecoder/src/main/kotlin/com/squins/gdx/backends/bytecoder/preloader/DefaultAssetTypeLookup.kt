package com.squins.gdx.backends.bytecoder.preloader

import java.io.File

class DefaultAssetTypeLookup : AssetTypeLookup {

    override fun getType(file: File): AssetType {
        val extension = file.extension.toLowerCase()
        if (isImage(extension)) return AssetType.Image
        if (isAudio(extension)) return AssetType.Audio
        return if (isText(extension)) AssetType.Text else AssetType.Binary
    }

    private fun isImage(extension: String): Boolean =
            extension == "jpg" || extension == "jpeg" || extension == "png" || extension == "bmp" || extension == "gif"

    private fun isText(extension: String): Boolean =
            extension == "json" || extension == "xml" || extension == "txt" || extension == "glsl" || extension == "fnt" || extension == "pack" || extension == "obj" || extension == "atlas" || extension == "g3dj"

    private fun isAudio(extension: String): Boolean = extension == "mp3" || extension == "ogg" || extension == "wav"

}