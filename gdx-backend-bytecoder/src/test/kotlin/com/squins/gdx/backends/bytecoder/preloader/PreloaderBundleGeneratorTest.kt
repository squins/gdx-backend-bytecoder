package com.squins.gdx.backends.bytecoder.preloader

import org.apache.tika.Tika
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import resolveProjectRootDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class PreloaderBundleGeneratorTest{
    val assetsDir = File(resolveProjectRootDir(), "/src/test/resources/assets")
    private val outputDirectory = File(resolveProjectRootDir(), "target/test-classes/PreloaderBundleGeneratorTestAssets")
    
    init {
        outputDirectory.mkdirs()
    }

    @AfterEach
    fun tearDown() {
        outputDirectory.deleteRecursively()
    }

    @Test
    fun generatorPopulatesAssetsInDir() {
        val generateAssets = PreloaderBundleGenerator(assetsDir, outputDirectory).generateAssets(assetsDir)

        println(generateAssets)
    }

    fun name() {
        echoMimeType("sample.mp3")
        echoMimeType("badlogic.jpg")
    }

    private fun echoMimeType(name: String) {
        val file = File(assetsDir, name)
        val tika =  Tika()

        val mimeType: String = tika.detect(file)
        println("Tika mimeType: $mimeType")
    }

    @Test
    fun testCheckOutputPathContents() {
        PreloaderBundleGenerator(assetsDir, outputDirectory).generate()

        val expectedContents = """
            i:badlogic.jpg:68465:image/jpeg:1
            a:sample.mp3:646974:audio/mpeg:1
        """.trimIndent()
                .split("\n")
                .toSet()

        assertEquals(expectedContents, File(outputDirectory, "/assets.txt").readLines().toSet())
    }

    @Test
    fun testCheckFileWithIncorrectTypes() {
        PreloaderBundleGenerator(assetsDir, outputDirectory).generate()

        val content = """
            i:badlogic.jpg:68465:audio/mpeg:1
            a:sample.mp3:646974:image/jpeg:1
        """.trimIndent()

        assertNotEquals(content, File(outputDirectory, "/assets.txt").readText())
    }
}


