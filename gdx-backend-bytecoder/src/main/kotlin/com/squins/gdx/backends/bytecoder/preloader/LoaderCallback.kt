package com.squins.gdx.backends.bytecoder.preloader

interface LoaderCallback<T> {
    fun success(result: T)

    fun error()
}