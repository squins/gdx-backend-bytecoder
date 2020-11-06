package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.ApplicationLogger

class BytecoderApplicationLogger : ApplicationLogger {

    override fun log(tag: String, message: String) {
        println("[$tag] $message")
    }

    override fun log(tag: String, message: String, exception: Throwable) {
        println("[$tag] $message, exception: $exception")
        exception.printStackTrace(System.out)
    }

    override fun error(tag: String, message: String) {
        System.err.println("[$tag]$message")
    }

    override fun error(tag: String, message: String, exception: Throwable) {
        System.err.println("[$tag] $message, exception: $exception")
        exception.printStackTrace(System.err)
    }

    override fun debug(tag: String, message: String) {
        println("[$tag] $message")
    }

    override fun debug(tag: String, message: String, exception: Throwable) {
        println("[$tag] $message, exception: $exception")
        exception.printStackTrace(System.out)
    }
}
