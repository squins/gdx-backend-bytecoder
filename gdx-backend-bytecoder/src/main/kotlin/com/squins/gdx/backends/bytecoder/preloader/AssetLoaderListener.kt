package com.squins.gdx.backends.bytecoder.preloader

interface AssetLoaderListener<T> {
    fun onProgress(amount: Double)
    fun onFailure()
    fun onSuccess(result: T)
}