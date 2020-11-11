package com.squins.gdx.backends.bytecoder.preloader

class Asset(val file: String,
            val url: String,
            val type: AssetFilter.AssetType,
            val size: Long,
            val mimeType: String) {
    var succeed = false
    var failed = false
    var downloadStarted = false
    var loaded: Long = 0

}