package com.squins.gdx.backends.bytecoder.preloader

import org.junit.jupiter.api.Test
import resolveProjectRootDir
import java.io.File
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
        val name = "sample.mp3"
        echoMimeType(name)
        echoMimeType("badlogic.jpg")
    }

    private fun echoMimeType(name: String) {
        val file = File(assetsDir, name)
        println("Exists? ${file.exists()}")
        println(Files.probeContentType(file.toPath()))
    }
}


