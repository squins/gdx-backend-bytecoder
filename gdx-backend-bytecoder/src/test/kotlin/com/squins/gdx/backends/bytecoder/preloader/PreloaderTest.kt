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

    @Test
    fun convertAssetsFailsAsExpected() {
        val assets = Preloader.convertToAssets("""
                i:sample.jpg:68465:image/jpeg:1
                a:sample.mp3:646974:audio/mp3:0""".trimIndent())

        assertNotEquals(listOf(
                Asset("sample.jpg", AssetType.Audio, 68465, "image/jpeg", true),
                Asset("sample.mp3", AssetType.Image, 646974, "audio/mp3", false)
        ), assets)

        assertNotEquals(listOf(
                Asset("sample.jpg", AssetType.Image, 68465, "sample.mp3", true),
                Asset("sample.mp3", AssetType.Audio, 646974, "audio/mp3", false)
        ), assets)
    }
}