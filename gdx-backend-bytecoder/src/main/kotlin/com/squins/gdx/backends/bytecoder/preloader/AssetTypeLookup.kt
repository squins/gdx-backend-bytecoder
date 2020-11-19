package com.squins.gdx.backends.bytecoder.preloader

import java.io.File

interface AssetTypeLookup {

    fun getType(file: File): AssetType
}