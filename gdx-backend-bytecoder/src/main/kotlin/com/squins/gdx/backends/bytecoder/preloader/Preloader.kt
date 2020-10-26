package com.squins.gdx.backends.bytecoder.preloader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.ObjectMap
import com.squins.gdx.backends.bytecoder.api.web.ImageElement
import com.squins.gdx.backends.bytecoder.preloader.AssetDownloader.AssetLoaderListener
import com.squins.gdx.backends.bytecoder.preloader.AssetFilter.AssetType
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


class Preloader {
    private val loader: AssetDownloader = AssetDownloader()

    interface PreloaderCallback {
        fun update(state: PreloaderState?)
        fun error(file: String?)
    }

    var directories: ObjectMap<String, Void> = ObjectMap<String, Void>()
    var images: ObjectMap<String, ImageElement> = ObjectMap<String, ImageElement>()
    var audio: ObjectMap<String, Blob> = ObjectMap<String, Blob>()
    var texts: ObjectMap<String, String> = ObjectMap<String, String>()
    var binaries: ObjectMap<String, Blob> = ObjectMap<String, Blob>()
    private val stillToFetchAssets: ObjectMap<String, Asset> = ObjectMap<String, Asset>()
    var assetNames: ObjectMap<String, String> = ObjectMap<String, String>()

    class Asset(val file: String, val url: String, type: AssetType, size: Long, mimeType: String) {
        var succeed = false
        var failed = false
        var downloadStarted = false
        var loaded: Long = 0
        val type: AssetType
        val size: Long
        val mimeType: String

        init {
            this.type = type
            this.size = size
            this.mimeType = mimeType
        }
    }

    class PreloaderState(val assets: Array<Asset>) {
        val downloadedSize: Long
            get() {
                var size: Long = 0
                for (i in 0 until assets.size) {
                    val asset = assets[i]
                    size += if (asset.succeed || asset.failed) asset.size else Math.min(asset.size, asset.loaded)
                }
                return size
            }

        val totalSize: Long
            get() {
                var size: Long = 0
                for (i in 0 until assets.size) {
                    val asset = assets[i]
                    size += asset.size
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

    var baseUrl: String? = null


    fun Preloader(newBaseURL: String?) {
        baseUrl = newBaseURL

        // trigger copying of assets and creation of assets.txt
//        GWT.create(PreloaderBundle::class.java)
    }

    fun preload(assetFileUrl: String, callback: PreloaderCallback) {
        loader.loadText(baseUrl + assetFileUrl + "?etag=" + System.currentTimeMillis(), object : AssetLoaderListener<String> {
            override fun onProgress(amount: Double) {}
            override fun onFailure() {
                callback.error(assetFileUrl)
            }

            override fun onSuccess(result: String) {
                val lines = result.split("\n".toRegex()).toTypedArray()
                val assets = mutableListOf<Asset>()
                for (line in lines) {
                    val tokens = line.split(":".toRegex()).toTypedArray()
                    if (tokens.size != 6) {
                        throw GdxRuntimeException("Invalid assets description file.")
                    }
                    val assetTypeCode = tokens[0]
                    val assetPathOrig = tokens[1]
                    val assetPathMd5 = tokens[2]
                    var size = tokens[3].toLong()
                    val assetMimeType = tokens[4]
                    val assetPreload = tokens[5] == "1"
                    var type: AssetType = AssetType.Text
                    if (assetTypeCode == "i") type = AssetType.Image
                    if (assetTypeCode == "b") type = AssetType.Binary
                    if (assetTypeCode == "a") type = AssetType.Audio
                    if (assetTypeCode == "d") type = AssetType.Directory
                    if (type === AssetType.Audio && !loader.isUseBrowserCache()) {
                        size = 0
                    }
                    val asset = Asset(assetPathOrig.trim { it <= ' ' }, assetPathMd5.trim { it <= ' ' }, type, size, assetMimeType)
                    assetNames.put(asset.file, asset.url)
                    if (assetPreload || asset.file.startsWith("com/badlogic/")) assets.add(asset) else stillToFetchAssets.put(asset.file, asset)
                }
                val state = PreloaderState(assets.toTypedArray())
                for (i in 0 until assets.size) {
                    val asset = assets[i]
                    if (contains(asset.file)) {
                        asset.loaded = asset.size
                        asset.succeed = true
                        continue
                    }
                    asset.downloadStarted = true
                    loader.load(baseUrl + asset.url, asset.type, asset.mimeType, object : AssetLoaderListener<Any?> {
                        override fun onProgress(amount: Double) {
                            asset.loaded = amount.toLong()
                            callback.update(state)
                        }

                        override fun onFailure() {
                            asset.failed = true
                            callback.error(asset.file)
                            callback.update(state)
                        }

                        override fun onSuccess(result: Any?) {
                            putAssetInMap(result, asset)
                            asset.succeed = true
                            callback.update(state)
                        }
                    })
                }
                callback.update(state)
            }
        })
    }

    fun preloadSingleFile(file: String?) {
        if (!isNotFetchedYet(file)) return
        val asset: Asset = stillToFetchAssets.get(file)
        if (asset.downloadStarted) return
        Gdx.app.log("Preloader", "Downloading " + baseUrl + asset.file)
        asset.downloadStarted = true
        loader.load(baseUrl + asset.url, asset.type, asset.mimeType, object : AssetLoaderListener<Any?>() {
            fun onProgress(amount: Double) {
                asset.loaded = amount.toLong()
            }

            fun onFailure() {
                asset.failed = true
                stillToFetchAssets.remove(file)
            }

            fun onSuccess(result: Any?) {
                putAssetInMap(result, asset)
                stillToFetchAssets.remove(file)
                asset.succeed = true
            }
        })
    }

    protected fun putAssetInMap(result: Any?, asset: Asset) {
        when (asset.type) {
            Text -> texts.put(asset.file, result as String?)
            Image -> images.put(asset.file, result as ImageElement?)
            Binary -> binaries.put(asset.file, result as Blob?)
            Audio -> audio.put(asset.file, result as Blob?)
            Directory -> directories.put(asset.file, null)
        }
    }

    fun read(file: String?): InputStream? {
        if (texts.containsKey(file)) {
            return try {
                ByteArrayInputStream(texts.get(file).getBytes("UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                null
            }
        }
        if (images.containsKey(file)) {
            return ByteArrayInputStream(ByteArray(1)) // FIXME, sensible?
        }
        if (binaries.containsKey(file)) {
            return binaries.get(file).read()
        }
        return if (audio.containsKey(file)) {
            audio.get(file).read()
        } else null
    }

    operator fun contains(file: String?): Boolean {
        return texts.containsKey(file) || images.containsKey(file) || binaries.containsKey(file) || audio.containsKey(file) || directories.containsKey(file)
    }

    fun isNotFetchedYet(file: String?): Boolean {
        return stillToFetchAssets.containsKey(file)
    }

    fun isText(file: String?): Boolean {
        return texts.containsKey(file)
    }

    fun isImage(file: String?): Boolean {
        return images.containsKey(file)
    }

    fun isBinary(file: String?): Boolean {
        return binaries.containsKey(file)
    }

    fun isAudio(file: String?): Boolean {
        return audio.containsKey(file)
    }

    fun isDirectory(file: String?): Boolean {
        return directories.containsKey(file)
    }

    private fun isChild(filePath: String, directory: String): Boolean {
        return filePath.startsWith("$directory/") && filePath.indexOf('/', directory.length + 1) < 0
    }

    fun list(file: String): Array<FileHandle?>? {
        return getMatchedAssetFiles(object : FilePathFilter {
            fun accept(path: String): Boolean {
                return isChild(path, file)
            }
        })
    }

    fun list(file: String, filter: FileFilter): Array<FileHandle?>? {
        return getMatchedAssetFiles(object : FilePathFilter {
            fun accept(path: String): Boolean {
                return isChild(path, file) && filter.accept(File(path))
            }
        })
    }

    fun list(file: String, filter: FilenameFilter): Array<FileHandle?>? {
        return getMatchedAssetFiles(object : FilePathFilter {
            fun accept(path: String): Boolean {
                return isChild(path, file) && filter.accept(File(file), path.substring(file.length + 1))
            }
        })
    }

    fun list(file: String, suffix: String?): Array<FileHandle?>? {
        return getMatchedAssetFiles(object : FilePathFilter {
            fun accept(path: String): Boolean {
                return isChild(path, file) && path.endsWith(suffix!!)
            }
        })
    }

    fun length(file: String?): Long {
        if (texts.containsKey(file)) {
            return try {
                texts.get(file).getBytes("UTF-8").length
            } catch (e: UnsupportedEncodingException) {
                texts.get(file).getBytes().length
            }
        }
        if (images.containsKey(file)) {
            return 1 // FIXME, sensible?
        }
        if (binaries.containsKey(file)) {
            return binaries.get(file).length()
        }
        return if (audio.containsKey(file)) {
            audio.get(file).length()
        } else 0
    }

    private interface FilePathFilter {
        fun accept(path: String?): Boolean
    }

    private fun getMatchedAssetFiles(filter: FilePathFilter): Array<FileHandle?>? {
        val files: Array<FileHandle> = Array<FileHandle>()
        for (file in assetNames.keys()) {
            if (filter.accept(file)) {
                files.add(GwtFileHandle(this, file, FileType.Internal))
            }
        }
        val filesArray: Array<FileHandle?> = arrayOfNulls<FileHandle>(files.size)
        System.arraycopy(files.items, 0, filesArray, 0, filesArray.size)
        return filesArray
    }
}