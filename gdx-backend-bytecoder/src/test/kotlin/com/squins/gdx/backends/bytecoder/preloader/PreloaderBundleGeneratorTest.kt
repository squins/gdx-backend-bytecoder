package com.squins.gdx.backends.bytecoder.preloader

import org.apache.tika.Tika
import org.junit.jupiter.api.AfterEach
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
    internal fun tearDown() {
        outputDirectory.deleteRecursively()
    }

    /*
    TODO coen:

    - read output path contents, see if contents equals expectations
    - make nested directories work (maybe it does already, we don't now how walker works?)
     */

    // this can be removed
    @Test
    internal fun generatorPopulatesAssetsInDir() {
        val generateAssets = PreloaderBundleGenerator(assetsDir, outputDirectory).generateAssets(assetsDir)

        println(generateAssets)
    }
    internal fun name() {
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
    internal fun testCheckOutputPathContents() {
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
    internal fun testCheckFileWithCorrectTypes() {
        val content = """
            i:badlogic.jpg:68465:audio/mpeg:1
            a:sample.mp3:646974:image/jpeg:1
        """.trimIndent()

        assertNotEquals(content, File(outputDirectory, "/assets.txt").readText())
    }
}


