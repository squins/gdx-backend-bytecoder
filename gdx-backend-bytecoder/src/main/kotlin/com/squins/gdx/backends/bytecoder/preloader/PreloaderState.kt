package com.squins.gdx.backends.bytecoder.preloader

class PreloaderState(val assets: List<Asset>) {
    private val downloadedSize: Long
        get() {
            var size: Long = 0
            for (element in assets) {
                size += if (element.succeed || element.failed) element.size else element.size.coerceAtMost(element.loaded)
            }
            return size
        }

    private val totalSize: Long
        get() {
            var size: Long = 0
            for (element in assets) {
                size += element.size
            }
            return size
        }

    val progress: Float
        get() {
            val total = totalSize
            return if (total == 0L) 1F else downloadedSize / total.toFloat()
        }

    fun hasEnded(): Boolean {
        return downloadedSize == totalSize
    }

}
