package com.squins.gdx.backends.bytecoder.preloader

class PreloaderState(val assets: List<Asset>) {
    private val downloadedSize: Long
        get() {
            var size: Long = 0
            for (element in assets) {
                size += if (element.succeed || element.failed) element.sizeInBytes else element.sizeInBytes.coerceAtMost(element.bytesLoaded)
            }
            return size
        }

    private val totalSize: Long
        get() {
            var size: Long = 0
            for (element in assets) {
                size += element.sizeInBytes
            }
            return size
        }

    val progress: Float
        get() {
            val total = totalSize
            return if (total == 0L) 1F else downloadedSize / total.toFloat()
        }

    fun hasEnded(): Boolean {
        println("HasEnded: downloaded: $downloadedSize, totalSize: $totalSize")
        return downloadedSize == totalSize
    }

}
