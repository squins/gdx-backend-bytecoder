package com.squins.gdx.backends.bytecoder.preloader

import org.junit.jupiter.api.Test
import java.io.File

class PreloaderBundleGeneratorTest{
    private fun resolveTestAssetResourcesRootDir() = File("/src/test/resources").absolutePath

    @Test
    fun generatorMakesAssetsTextFile() {
        val resolveTestAssetResourcesRootDir = File("").absolutePath + "/src/test/resources/assets"
        println("resolveTestAssetResourcesRootDir: $resolveTestAssetResourcesRootDir")
        println("root:" + File(".").absolutePath)
        println("resolveProjectRootDir" + resolveTestAssetResourcesRootDir())
        PreloaderBundleGenerator(resolveTestAssetResourcesRootDir).generate()
    }
}