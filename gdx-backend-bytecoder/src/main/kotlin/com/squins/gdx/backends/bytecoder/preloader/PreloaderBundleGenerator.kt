package com.squins.gdx.backends.bytecoder.preloader

import org.apache.tika.Tika
import java.io.File


class PreloaderBundleGenerator(private val assetSourceDirectory: File, private val outputDirectory:File) {

    private val assetTypeLookup: AssetTypeLookup = DefaultAssetTypeLookup()
    private val tika = Tika()

    fun generate() {
        File(outputDirectory, "assets.txt")
                .writeText(
                        generateAssets(assetSourceDirectory)
                        .map {
                            convertAssetToLine(it)
                        }.joinToString("\n")
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
                        mimeType = tika.detect(it),
                        preloadEnabled = true
                )
            }
            .toList()

    fun getAssetType(file: File) : AssetType {
        return assetTypeLookup.getType(file)
    }

}