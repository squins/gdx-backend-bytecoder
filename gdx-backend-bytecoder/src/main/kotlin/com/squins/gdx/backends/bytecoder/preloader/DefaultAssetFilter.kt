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

class DefaultAssetFilter : AssetFilter {
    private fun extension(file: String): String {
        val dotIndex = file.lastIndexOf('.')
        return if (dotIndex == -1) "" else file.substring(dotIndex + 1)
    }

    override fun accept(file: String, isDirectory: Boolean): Boolean {
        val normFile = file.replace('\\', '/')
        if (normFile.contains("/.")) return false
        if (normFile.contains("/_")) return false
        return !(isDirectory && file.endsWith(".svn"))
    }

    override fun preload(file: String): Boolean {
        return true
    }

    override fun getType(file: String): AssetFilter.AssetType {
        val extension = extension(file).toLowerCase()
        if (isImage(extension)) return AssetFilter.AssetType.Image
        if (isAudio(extension)) return AssetFilter.AssetType.Audio
        return if (isText(extension)) AssetFilter.AssetType.Text else AssetFilter.AssetType.Binary
    }

    private fun isImage(extension: String): Boolean {
        return extension == "jpg" || extension == "jpeg" || extension == "png" || extension == "bmp" || extension == "gif"
    }

    private fun isText(extension: String): Boolean {
        return extension == "json" || extension == "xml" || extension == "txt" || extension == "glsl" || extension == "fnt" || extension == "pack" || extension == "obj" || extension == "atlas" || extension == "g3dj"
    }

    private fun isAudio(extension: String): Boolean {
        return extension == "mp3" || extension == "ogg" || extension == "wav"
    }

    override fun getBundleName(file: String): String {
        return "assets"
    }
}