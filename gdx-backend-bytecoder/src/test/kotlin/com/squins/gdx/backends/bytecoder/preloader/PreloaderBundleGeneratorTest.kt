package com.squins.gdx.backends.bytecoder.preloader

import org.apache.tika.Tika
import org.junit.jupiter.api.Test
import resolveProjectRootDir
import java.io.File
import java.net.URLConnection
import java.nio.file.Files
import kotlin.test.assertEquals


class PreloaderBundleGeneratorTest{
    val assetsDir = File(resolveProjectRootDir(), "/src/test/resources/assets")

    @Test
    fun generatorMakesAssetsTextFile() {
        println("user dir: " + System.getProperty("user.dir"))
        println("root:" + File(".").absolutePath)
        val classpathFiles = PreloaderBundleGenerator(assetsDir).getClasspathFiles(assetsDir)

        assertEquals(listOf("badlogic.jpg", "sample.mp3"), classpathFiles)
        println(classpathFiles)

        assertEquals("a", "a")
    }

    @Test
    internal fun generatorPopulatesAssetsInDir() {
        val generateAssets = PreloaderBundleGenerator(assetsDir).generateAssets(assetsDir)

        println(generateAssets)
    }

    @Test
    internal fun name() {
        echoMimeType("sample.mp3")
        echoMimeType("sample2.mp3")
        echoMimeType("badlogic.jpg")
    }

    private fun echoMimeType(name: String) {
        val file = File(assetsDir, name)
        println("File ${file} exists? ${file.exists()}")

        println("prope: " + Files.probeContentType(file.toPath()))

        println("""url connection: ${URLConnection.getFileNameMap().getContentTypeFor(file.name)}""")

        val tika = Tika()
        val mimeType: String = tika.detect(file)
        println("Tika mimeType: ${mimeType}")

    }
}


