/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squins.gdx.backends.bytecoder.preloader

import org.apache.tika.Tika
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


const val tagPBG = "PreloaderBundleGenerator"

class PreloaderBundleGenerator(private val assetSourceDirectory: File, private val outputDirectory:File) {

    private val assetFilter: AssetFilter = DefaultAssetFilter()
    private val tika = Tika()

    fun generate() {
        File(outputDirectory, "assets.txt").writeText(generateAssets(assetSourceDirectory)
                .map {
                    convertAssetToLine(it)
                }.joinToString().replace(",", "\n").replace(" ", ""))

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

    fun getAssetType(file: File) : AssetFilter.AssetType {
        return assetFilter.getType(file.name)
    }

    fun getClasspathFiles(directory: File): List<String>
            = directory.walk()
             .filter { it.isFile }
             .map {it.name}
             .toList()
             .sorted()

    companion object {
//        private fun fileNameWithMd5(fw: FileWrapper, bytes: ByteArray): String {
//            val md5: String
//            md5 = try {
//                val digest = MessageDigest.getInstance("MD5")
//                digest.update(bytes)
//                String.format("%032x", BigInteger(1, digest.digest()))
//            } catch (e: NoSuchAlgorithmException) {
//                // Fallback
//                System.currentTimeMillis().toString()
//            }
//            var nameWithMd5: String = fw.nameWithoutExtension() + "-" + md5
//            val extension: String = fw.extension()
//            if (extension.isNotEmpty() || fw.name().endsWith(".")) {
//                nameWithMd5 = "$nameWithMd5.$extension"
//            }
//            return nameWithMd5
//        }
    }
}