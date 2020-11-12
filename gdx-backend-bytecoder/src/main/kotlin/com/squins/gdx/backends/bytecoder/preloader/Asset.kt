package com.squins.gdx.backends.bytecoder.preloader

data class Asset(val file: String,
                 val url: String,
                 val type: AssetFilter.AssetType,
                 val sizeInBytes: Long,
                 val mimeType: String,
                 val preloadEnabled:Boolean) {

    val shouldPreload = preloadEnabled || file.startsWith("com/badlogic/")
    var succeed = false
    var failed = false
    var downloadStarted = false
    var bytesLoaded: Long = 0
}