package com.squins.gdx.backends.bytecoder.preloader

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PreloaderTest {

    @Test
    fun convertAssetsWorksAsExpected() {


        val assets = Preloader.convertToAssets("""
                i:badlogic.jpg:68465:image/jpeg:1
                a:sample.mp3:646974:audio/mp3:0""".trimIndent())

        assertEquals(
                listOf(
                        Asset("badlogic.jpg",  AssetType.Image, 68465, "image/jpeg", true),
                        Asset("sample.mp3", AssetType.Audio,646974, "audio/mp3", false )
                ), assets)

    }

}