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

import com.badlogic.gdx.utils.StreamUtils
import com.squins.gdx.backends.bytecoder.makeAndLogIllegalArgumentException
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter
import java.math.BigInteger
import java.net.URLConnection
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/** Copies assets from the path specified in the modules gdx.assetpath configuration property to the war/ folder and generates the
 * assets.txt file. The type of a file is determined by an [AssetFilter], which is either created by instantiating the class
 * specified in the gdx.assetfilterclass property, or falling back to the [DefaultAssetFilter].
 * @author mzechner
 */

const val tagPBG = "PreloaderBundleGenerator"

class PreloaderBundleGenerator : com.google.gwt.core.ext.Generator() {
    private inner class Asset(var filePathOrig: String, var file: FileWrapper, var type: AssetFilter.AssetType) {
    }

    override fun generate(logger: TreeLogger, context: GeneratorContext, typeName: String): String {
        println(File(".").absolutePath)
        val assetPath = getAssetPath(context)
        var assetOutputPath = getAssetOutputPath(context)
        if (assetOutputPath == null) {
            assetOutputPath = "war/"
        }
        val assetFilter: AssetFilter = getAssetFilter(context)
        var source = FileWrapper(assetPath)
        if (!source.exists()) {
            source = FileWrapper("../$assetPath")
            if (!source.exists()) throw RuntimeException("assets path '" + assetPath
                    + "' does not exist. Check your gdx.assetpath property in your GWT project's module gwt.xml file")
        }
        if (!source.isDirectory) throw RuntimeException("assets path '" + assetPath
                + "' is not a directory. Check your gdx.assetpath property in your GWT project's module gwt.xml file")
        println("Copying resources from $assetPath to $assetOutputPath")
        println(source.file?.absolutePath)
        var target = FileWrapper("assets/") // this should always be the war/ directory of the GWT project.
        println(target.file?.absolutePath)
        if (!target.file?.absolutePath?.replace("\\", "/")?.endsWith(assetOutputPath + "assets")!!) {
            target = FileWrapper(assetOutputPath + "assets/")
        }
        if (target.exists()) {
            if (!target.deleteDirectory()) throw RuntimeException("Couldn't clean target path '$target'")
        }
        val assets = ArrayList<Asset>()
        copyDirectory(source, target, assetFilter, assets)

        // Now collect classpath files and copy to assets
        val classpathFiles = getClasspathFiles(context)
        for (classpathFile in classpathFiles) {
            if (assetFilter.accept(classpathFile, false)) {
                try {
                    val `is`: InputStream = context.javaClass.getClassLoader().getResourceAsStream(classpathFile)
                    val bytes = StreamUtils.copyStreamToByteArray(`is`)
                    `is`.close()
                    val origFile: FileWrapper = target.child(classpathFile)
                    val destFile: FileWrapper = target.child(fileNameWithMd5(origFile, bytes))
                    destFile.writeBytes(bytes, false)
                    assets.add(Asset(origFile.path(), destFile, assetFilter.getType(destFile.path())))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        val bundles = HashMap<String, ArrayList<Asset>>()
        for (asset in assets) {
            var bundleName: String? = assetFilter.getBundleName(asset.file.path())
            if (bundleName == null) {
                bundleName = "assets"
            }
            var bundleAssets = bundles[bundleName]
            if (bundleAssets == null) {
                bundleAssets = ArrayList()
                bundles[bundleName] = bundleAssets
            }
            bundleAssets.add(asset)
        }

        // Write the tokens for Preloader.preload()
        for ((key, value) in bundles) {
            val sb = StringBuilder()
            for (asset in value) {
                var pathOrig = asset.filePathOrig.replace('\\', '/').replace(assetOutputPath, "").replaceFirst("assets/".toRegex(), "")
                if (pathOrig.startsWith("/")) pathOrig = pathOrig.substring(1)
                var pathMd5: String = asset.file.path().replace('\\', '/').replace(assetOutputPath, "").replaceFirst("assets/".toRegex(), "")
                if (pathMd5.startsWith("/")) pathMd5 = pathMd5.substring(1)
                sb.append(asset.type.code)
                sb.append(":")
                sb.append(pathOrig)
                sb.append(":")
                sb.append(pathMd5)
                sb.append(":")
                sb.append(if (asset.file.isDirectory) 0 else asset.file.length())
                sb.append(":")
                val mimetype = URLConnection.guessContentTypeFromName(asset.file.name())
                sb.append(mimetype ?: "application/unknown")
                sb.append(":")
                sb.append(if (asset.file.isDirectory || assetFilter.preload(pathOrig)) '1' else '0')
                sb.append("\n")
            }
            target.child("$key.txt").writeString(sb.toString(), false)
        }
        return createDummyClass(logger, context)
    }

    private fun copyFile(source: FileWrapper, filePathOrig: String, dest: FileWrapper, filter: AssetFilter, assets: ArrayList<Asset>) {
        if (!filter.accept(filePathOrig, false)) return
        try {
            assets.add(Asset(filePathOrig, dest, filter.getType(dest.path())))
            dest.write(source.read(), false)
        } catch (ex: Exception) {
            throw makeAndLogIllegalArgumentException(tagPBG, """
    Error copying source file: $source
    To destination: $dest
    Exp: $ex
    """.trimIndent())
        }
    }

    private fun copyDirectory(sourceDir: FileWrapper, destDir: FileWrapper, filter: AssetFilter, assets: ArrayList<Asset>) {
        if (!filter.accept(destDir.path(), true)) return
        assets.add(Asset(destDir.path(), destDir, AssetFilter.AssetType.Directory))
        destDir.mkdirs()
        val files: Array<FileWrapper?> = sourceDir.list()
        var i = 0
        val n = files.size
        while (i < n) {
            val srcFile: FileWrapper = files[i]!!
            if (srcFile.isDirectory) {
                val destFile: FileWrapper = destDir.child(srcFile.name())
                copyDirectory(srcFile, destFile, filter, assets)
            } else {
                val destFile: FileWrapper = destDir.child(fileNameWithMd5(srcFile, srcFile.readBytes()))
                copyFile(srcFile, destDir.child(srcFile.name()).path(), destFile, filter, assets)
            }
            i++
        }
    }

    private fun getAssetFilter(context: GeneratorContext): AssetFilter {
        var assetFilterClassProperty: com.google.gwt.core.ext.ConfigurationProperty? = null
        assetFilterClassProperty = try {
            context.getPropertyOracle().getConfigurationProperty("gdx.assetfilterclass")
        } catch (e: BadPropertyValueException) {
            return DefaultAssetFilter()
        }
        if (assetFilterClassProperty.getValues().size == 0) {
            return DefaultAssetFilter()
        }
        val assetFilterClass: String = assetFilterClassProperty.getValues().get(0) ?: return DefaultAssetFilter()
        return try {
            Class.forName(assetFilterClass).newInstance() as AssetFilter
        } catch (e: Exception) {
            throw RuntimeException("Couldn't instantiate custom AssetFilter '" + assetFilterClass
                    + "', make sure the class is public and has a public default constructor", e)
        }
    }

    private fun getAssetPath(context: GeneratorContext): String {
        var assetPathProperty: com.google.gwt.core.ext.ConfigurationProperty? = null
        assetPathProperty = try {
            context.getPropertyOracle().getConfigurationProperty("gdx.assetpath")
        } catch (e: BadPropertyValueException) {
            throw RuntimeException(
                    "No gdx.assetpath defined. Add <set-configuration-property name=\"gdx.assetpath\" value=\"relative/path/to/assets/\"/> to your GWT projects gwt.xml file")
        }
        if (assetPathProperty.getValues().size == 0) {
            throw RuntimeException(
                    "No gdx.assetpath defined. Add <set-configuration-property name=\"gdx.assetpath\" value=\"relative/path/to/assets/\"/> to your GWT projects gwt.xml file")
        }
        val paths: String? = assetPathProperty.getValues().get(0)
        if (paths == null) {
            throw RuntimeException(
                    "No gdx.assetpath defined. Add <set-configuration-property name=\"gdx.assetpath\" value=\"relative/path/to/assets/\"/> to your GWT projects gwt.xml file")
        } else {
            val existingPaths = ArrayList<String>()
            val tokens = paths.split(",".toRegex()).toTypedArray()
            for (token in tokens) {
                println(token)
                if (FileWrapper(token).exists() || FileWrapper("../$token").exists()) {
                    return token
                }
            }
            throw RuntimeException(
                    "No valid gdx.assetpath defined. Fix <set-configuration-property name=\"gdx.assetpath\" value=\"relative/path/to/assets/\"/> in your GWT projects gwt.xml file")
        }
    }

    private fun getAssetOutputPath(context: GeneratorContext): String? {
        var assetPathProperty: com.google.gwt.core.ext.ConfigurationProperty? = null
        assetPathProperty = try {
            context.getPropertyOracle().getConfigurationProperty("gdx.assetoutputpath")
        } catch (e: BadPropertyValueException) {
            return null
        }
        if (assetPathProperty.getValues().size == 0) {
            return null
        }
        val paths: String? = assetPathProperty.getValues().get(0)
        return if (paths == null) {
            null
        } else {
            val existingPaths = ArrayList<String>()
            val tokens = paths.split(",".toRegex()).toTypedArray()
            var path: String? = null
            for (token in tokens) {
                if (FileWrapper(token).exists() || FileWrapper(token).mkdirs()) {
                    path = token
                }
            }
            if (path != null && !path.endsWith("/")) {
                path += "/"
            }
            path
        }
    }

    private fun getClasspathFiles(context: GeneratorContext): List<String> {
        val classpathFiles: MutableList<String> = ArrayList()
        try {
            val prop: com.google.gwt.core.ext.ConfigurationProperty = context.getPropertyOracle().getConfigurationProperty("gdx.files.classpath")
            for (value in prop.getValues()) {
                classpathFiles.add(value)
            }
        } catch (e: BadPropertyValueException) {
            // Ignore
        }
        return classpathFiles
    }

    private fun createDummyClass(logger: TreeLogger, context: GeneratorContext): String {
        val packageName = "com.badlogic.gdx.backends.gwt.preloader"
        val className = "PreloaderBundleImpl"
        val composer = ClassSourceFileComposerFactory(packageName, className)
        composer.addImplementedInterface("$packageName.PreloaderBundle")
        val printWriter: PrintWriter = context.tryCreate(logger, packageName, className)
                ?: return "$packageName.$className"
        val sourceWriter: com.google.gwt.user.rebind.SourceWriter = composer.createSourceWriter(context, printWriter)
        sourceWriter.commit(logger)
        return "$packageName.$className"
    }

    companion object {
        private fun fileNameWithMd5(fw: FileWrapper, bytes: ByteArray): String {
            val md5: String
            md5 = try {
                val digest = MessageDigest.getInstance("MD5")
                digest.update(bytes)
                String.format("%032x", BigInteger(1, digest.digest()))
            } catch (e: NoSuchAlgorithmException) {
                // Fallback
                System.currentTimeMillis().toString()
            }
            var nameWithMd5: String = fw.nameWithoutExtension() + "-" + md5
            val extension: String = fw.extension()
            if (extension.isNotEmpty() || fw.name().endsWith(".")) {
                nameWithMd5 = "$nameWithMd5.$extension"
            }
            return nameWithMd5
        }
    }
}