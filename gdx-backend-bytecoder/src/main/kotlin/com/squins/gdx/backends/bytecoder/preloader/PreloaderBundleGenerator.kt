package com.squins.gdx.backends.bytecoder.preloader

import org.apache.tika.Tika
import java.io.File
import java.net.URLConnection


class PreloaderBundleGenerator(private val assetSourceDirectory: File, private val outputDirectory:File) {

    private val assetTypeLookup: AssetTypeLookup = DefaultAssetTypeLookup()
    private val tika = Tika()

    fun generate() {
        outputDirectory.mkdirs()
        File(outputDirectory, "assets.txt")
                .writeText(
                        generateAssets(assetSourceDirectory).joinToString("\n") {
                            convertAssetToLine(it)
                        }
                )

        assetSourceDirectory.copyRecursively(outputDirectory, true)
    }

    private fun convertAssetToLine(it: Asset) = it.type.code + ":" + it.file + ":" + it.sizeInBytes + ":" + it.mimeType + ":" + if(it.shouldPreload) { "1" } else {"0"}

    fun generateAssets(directory: File): List<Asset>
            = directory.walk()
            .filter { it.isFile }
            .map {
                Asset(
                        file=it.name,
                        type= getAssetType(it),
                        sizeInBytes = it.length(),
                        mimeType = if(URLConnection.guessContentTypeFromName(it.name) == null) {
                            "application/unknown" }
                        else { URLConnection.guessContentTypeFromName(it.name)},
                        preloadEnabled = true
                )
            }
            .toList()

    fun getAssetType(file: File) : AssetType {
        return assetTypeLookup.getType(file)
    }

}