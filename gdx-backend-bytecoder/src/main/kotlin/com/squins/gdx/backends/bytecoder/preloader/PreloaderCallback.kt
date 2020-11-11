package com.squins.gdx.backends.bytecoder.preloader

interface PreloaderCallback {
    fun update(state: PreloaderState)
    fun error(file: String)
}