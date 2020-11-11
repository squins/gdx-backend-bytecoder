package com.squins.gdx.backends.bytecoder.preloader

import com.badlogic.gdx.Files
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.ObjectMap
import com.squins.gdx.backends.bytecoder.BytecoderFileHandle
import com.squins.gdx.backends.bytecoder.api.web.HtmlAudioElement
import com.squins.gdx.backends.bytecoder.api.web.HtmlImageElement
import com.squins.gdx.backends.bytecoder.makeAndLogIllegalArgumentException
import com.squins.gdx.backends.bytecoder.preloader.AssetFilter.AssetType
import de.mirkosertic.bytecoder.api.web.Window
import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter


class Preloader(private val baseUrl:String) {
    private val loader: AssetDownloader = AssetDownloader()
    private var directories: ObjectMap<String, Void> = ObjectMap()
    val images: ObjectMap<String, HtmlImageElement> = ObjectMap()
    val audio: ObjectMap<String, HtmlAudioElement> = ObjectMap()
    val texts: ObjectMap<String, String> = ObjectMap()
    val binaries: ObjectMap<String, Blob> = ObjectMap()
    private val stillToFetchAssets: ObjectMap<String, Asset> = ObjectMap()
    private val assetNames: ObjectMap<String, String> = ObjectMap()


    init {
        // DISABLED: performance println("try put a and b in main")
        assetNames.put("a", "b")
    }

    fun preload(assetFileUrl: String, callback: PreloaderCallback) {
        // DISABLED: performance println("preload called")
        Window.window().fetch(assetFileUrl).then { response ->
            // DISABLED: performance println("Data received")
            response.text().then { responseText ->
                // DISABLED: performance println("String data is $responseText")
                val assets: MutableList<Asset> = handleAsset(responseText)
                // DISABLED: performance println("before state = PreloaderState(assets)")
                val state = PreloaderState(assets)
                // DISABLED: performance println("after state = PreloaderState(assets)")
                for(i in assets.indices){
                    val asset = assets[i]
                    // DISABLED: performance println("before contain asset file")
                    println(asset.file)
                    if(contains(asset.file)) {
                        // DISABLED: performance println("true")
                        asset.loaded = asset.size
                        asset.succeed = true
                        continue
                    }

                    asset.downloadStarted = true
                    // DISABLED: performance println("before loader.load")
                    loader.load(baseUrl + "/" + asset.url, asset.type, asset.mimeType, object: AssetLoaderListener<Any>{
                        override fun onProgress(amount: Double) {
                            // DISABLED: performance println("onProgress")
                            asset.loaded = amount.toLong()
                            callback.update(state)
                        }
                        override fun onFailure() {
                            // DISABLED: performance println("onFailure")
                            asset.failed = true
                            callback.error(asset.file)
                            callback.update(state)
                        }

                        override fun onSuccess(result: Any) {
                            // DISABLED: performance println("onSuccess")
                            putAssetInMap(result, asset)
                            asset.succeed = true
                            callback.update(state)
                        }

                    })
                }
                callback.update(state)
            }
        }
        // DISABLED: performance println("assetFileUrl: $assetFileUrl")
//        loader.loadText(assetFileUrl + "?etag=" + System.currentTimeMillis(), object : AssetLoaderListener<String> {
//            override fun onProgress(amount: Double) {}
//            override fun onFailure() {
//                callback.error(assetFileUrl)
//            }
//
//            override fun onSuccess(result: String) {
//                val assets: MutableList<Asset> = handleAsset(result)
//                println("before state = PreloaderState(assets)")
//                val state = PreloaderState(assets)
//                println("after state = PreloaderState(assets)")
//                for(i in assets.indices){
//                    val asset = assets[i]
//                    println("before contain asset file")
//                    println(asset.file)
//                    if(contains(asset.file)) {
//                        println("true")
//                        asset.loaded = asset.size
//                        asset.succeed = true
//                        continue
//                    }
//
//                    asset.downloadStarted = true
//                    println("before loader.load")
//                    loader.load(baseUrl + asset.url, asset.type, asset.mimeType, object: AssetLoaderListener<Any>{
//                        override fun onProgress(amount: Double) {
//                            println("onProgress")
//                            asset.loaded = amount.toLong()
//                            callback.update(state)
//                        }
//                        override fun onFailure() {
//                            println("onFailure")
//                            asset.failed = true
//                            callback.error(asset.file)
//                            callback.update(state)
//                        }
//
//                        override fun onSuccess(result: Any) {
//                            println("onSuccess")
//                            putAssetInMap(result, asset)
//                            asset.succeed = true
//                            callback.update(state)
//                        }
//
//                    })
//                }
//                callback.update(state)
//            }
//        })
    }

    private fun handleAsset(result: String): MutableList<Asset> {
        // DISABLED: performance println("onSucces, result: $result")
        val lines = result.split("\n")
        // DISABLED: performance println("(lines[0])
        val assets = mutableListOf<Asset>()
        // DISABLED: performance println("after assets: ${assets.size}" )
        for (line in lines) {
            // DISABLED: performance println("line in lines: $line")
            val tokens = line.split(":")
            // DISABLED: performance println("(tokens.size)
            for(token in tokens.withIndex()){
                // DISABLED: performance println("(token.value)
            }
            if (tokens.size != 6) {
                // DISABLED: performance println("size not 6")
                throw makeAndLogIllegalArgumentException("Preloader","Invalid assets description file.")
            }
            // DISABLED: performance println("after check size")
            val assetTypeCode = tokens[0]
            // DISABLED: performance println("after assetTypeCode: $assetTypeCode")

            val assetPathOrig = tokens[1]
            // DISABLED: performance println("after assetPathOrig: $assetPathOrig")

            val assetPathMd5 = tokens[2]
            // DISABLED: performance println("after assetPathMd5: $assetPathMd5")

            var size = tokens[3].toLong()
            // DISABLED: performance println("after size: $size")

            val assetMimeType = tokens[4]
            // DISABLED: performance println("after assetMimeType: $assetMimeType")

            val assetPreload = tokens[5] == "1"
            // DISABLED: performance println("after assetPreload: $assetPreload")
            var type: AssetType = AssetType.Text
            if (assetTypeCode == "i") type = AssetType.Image
            if (assetTypeCode == "b") type = AssetType.Binary
            if (assetTypeCode == "a") type = AssetType.Audio
            if (assetTypeCode == "d") type = AssetType.Directory
            // DISABLED: performance println("after type checking, type is: ${type.code}")
            if (type === AssetType.Audio && !loader.isUseBrowserCache) {
                // DISABLED: performance println("audio and not isUseBrowserCache")
                size = 0
            }
            val asset = Asset(assetPathOrig.trim(), assetPathMd5.trim(), type, size, assetMimeType)
            // DISABLED: performance println("("after new asset, asset.file: ${asset.file}, asset.url: ${asset.url}")
            val assetFile = asset.file
            val assetUrl = asset.url
            // DISABLED: performance println("("$assetFile and $assetUrl")


            // DISABLED: performance println("("empty or not: ${assetNames.isEmpty()}")
            // DISABLED: performance println("("size: " + assetNames.size)

            assetNames.put(assetFile, assetUrl)

            // DISABLED: performance println("("after assetNames.put")
            if (assetPreload || asset.file.startsWith("com/badlogic/")) {
                // DISABLED: performance println("("before assets.add")
                assets.add(asset)
                // DISABLED: performance println("("after assets.add")
            }
            else {
                // DISABLED: performance println("("add to stillToFetchAssets")
                stillToFetchAssets.put(asset.file, asset)
            }
        }
        return assets
    }

    // TODO coen: this method is now directly called, change to load via assets.txt
    fun doLoadAssets(assets: List<Asset>, callback: PreloaderCallback) {
        // DISABLED: performance println("("doLoadAssets called, assets.size: ${assets.size}")
        val state = PreloaderState(assets)
        // DISABLED: performance println("("created state")
        for (element in assets) {
            // DISABLED: performance println("("loop, asset: ${element.file}")
            if (contains(element.file)) {
                element.loaded = element.size
                element.succeed = true
                continue
            }
            element.downloadStarted = true
            loader.load(baseUrl + "/" + element.url, element.type, element.mimeType, object : AssetLoaderListener<Any?> {
                override fun onProgress(amount: Double) {
                    // DISABLED: performance println("("onProgress")
                    element.loaded = amount.toLong()
                    callback.update(state)
                }

                override fun onFailure() {
                    // DISABLED: performance println("("onFailure")
                    element.failed = true
                    callback.error(element.file)
                    callback.update(state)
                }

                override fun onSuccess(result: Any?) {
                    // DISABLED: performance println("("onSuccess")
                    putAssetInMap(result, element)
                    element.succeed = true
                    callback.update(state)
                }
            })
        }
        callback.update(state)
    }

    fun preloadSingleFile(file: String) {
        if (!isNotFetchedYet(file)) return
        val asset: Asset = stillToFetchAssets.get(file)
        if (asset.downloadStarted) return
        // DISABLED: performance println("("""Downloading $baseUrl${asset.file}""")
        asset.downloadStarted = true
        loader.load(baseUrl + "/" + asset.url, asset.type, asset.mimeType, object : AssetLoaderListener<Any?> {
            override fun onProgress(amount: Double) {
                asset.loaded = amount.toLong()
            }

            override fun onFailure() {
                asset.failed = true
                stillToFetchAssets.remove(file)
            }

            override fun onSuccess(result: Any?) {
                putAssetInMap(result, asset)
                stillToFetchAssets.remove(file)
                asset.succeed = true
            }
        })
    }

    private fun putAssetInMap(result: Any?, asset: Asset) {
        // DISABLED: performance println("("putAssetInMap called(X)")
        // DISABLED: performance println("("putAssetInMap asset.file: ${asset.file}")
        // DISABLED: performance println("("result is null: before")
        // DISABLED: performance println("("result is null: ${result == null}")
        // DISABLED: performance println("("asset.type: ${asset.type}")
        // DISABLED: performance println("("asset.type.code: ${asset.type.code}")

        when (asset.type.code) {
            AssetType.Text.code -> texts.put(asset.file, result as String)
            AssetType.Image.code -> images.put(asset.file, result as HtmlImageElement)
            AssetType.Binary.code -> binaries.put(asset.file, result as Blob)
            AssetType.Audio.code -> audio.put(asset.file, result as HtmlAudioElement)
            AssetType.Directory.code -> directories.put(asset.file, null)
        }
        // DISABLED: performance println("("After putImageInMap when, sizes: texts: ${texts.size}, images: ${images.size}, audio: ${audio.size} ")
    }

//    fun read(file: String): InputStream? {
//        if (texts.containsKey(file)) {
//            return try {
//                ByteArrayInputStream(texts.get(file).getBytes("UTF-8"))
//            } catch (e: UnsupportedEncodingException) {
//                null
//            }
//        }
//        if (images.containsKey(file)) {
//            return ByteArrayInputStream(ByteArray(1)) // FIXME, sensible?
//        }
//        if (binaries.containsKey(file)) {
//            return binaries.get(file).read()
//        }
//        return if (audio.containsKey(file)) {
//            audio.get(file).read()
//        } else null
//    }

    operator fun contains(file: String): Boolean {
        return texts.containsKey(file) || images.containsKey(file) || binaries.containsKey(file) || audio.containsKey(file) || directories.containsKey(file)
    }

    private fun isNotFetchedYet(file: String): Boolean {
        return stillToFetchAssets.containsKey(file)
    }

    fun isText(file: String): Boolean {
        return texts.containsKey(file)
    }

    fun isImage(file: String): Boolean {
        return images.containsKey(file)
    }

    fun isBinary(file: String): Boolean {
        return binaries.containsKey(file)
    }

    fun isAudio(file: String): Boolean {
        return audio.containsKey(file)
    }

    fun isDirectory(file: String): Boolean {
        return directories.containsKey(file)
    }

    private fun isChild(filePath: String, directory: String): Boolean {
        return filePath.startsWith("$directory/") && filePath.indexOf('/', directory.length + 1) < 0
    }

    fun list(file: String): Array<FileHandle> {
        return getMatchedAssetFiles(object : FilePathFilter {
            override fun accept(path: String): Boolean {
                return isChild(path, file)
            }
        })
    }

    fun list(file: String, filter: FileFilter): Array<FileHandle> {
        return getMatchedAssetFiles(object : FilePathFilter {
            override fun accept(path: String): Boolean {
                return isChild(path, file) && filter.accept(File(path))
            }
        })
    }

    fun list(file: String, filter: FilenameFilter): Array<FileHandle> {
        return getMatchedAssetFiles(object : FilePathFilter {
            override fun accept(path: String): Boolean {
                return isChild(path, file) && filter.accept(File(file), path.substring(file.length + 1))
            }
        })
    }

    fun list(file: String, suffix: String): Array<FileHandle> {
        return getMatchedAssetFiles(object : FilePathFilter {
            override fun accept(path: String): Boolean {
                return isChild(path, file) && path.endsWith(suffix)
            }
        })
    }

//    fun length(file: String): Long {
//        if (texts.containsKey(file)) {
//            return try {
//                texts.get(file).getBytes("UTF-8").length
//            } catch (e: UnsupportedEncodingException) {
//                texts.get(file).getBytes().length
//            }
//        }
//        if (images.containsKey(file)) {
//            return 1 // FIXME, sensible?
//        }
//        if (binaries.containsKey(file)) {
//            return binaries.get(file).length().toLong()
//        }
//        return if (audio.containsKey(file)) {
//            audio.get(file).length
//        } else 0
//    }

    private interface FilePathFilter {
        fun accept(path: String): Boolean
    }

    private fun getMatchedAssetFiles(filter: FilePathFilter): Array<FileHandle> {
        // TODO: implement ok?
        val files: Array<FileHandle> = arrayOf()
        for (file in assetNames.keys()) {
            if (filter.accept(file)) {
                //fix set index needed
                mutableListOf<FileHandle>(BytecoderFileHandle(this, file, Files.FileType.Internal))
            }
        }
        val filesArray: Array<FileHandle> = arrayOf()
        System.arraycopy(files, 0, filesArray, 0, filesArray.size)
        return filesArray
//        return arrayOf()
    }

}