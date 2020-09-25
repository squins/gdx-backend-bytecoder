package com.libgdx.backends.bytecoder

import com.badlogic.gdx.*
import com.badlogic.gdx.utils.Clipboard


class BytecoderApplication(val listener: ApplicationListener, bytecoderGL20: BytecoderGL20) : Application {

    init {
        Gdx.app = this
        Gdx.gl = bytecoderGL20
        Gdx.gl20 = bytecoderGL20

        listener.create()
        listener.render()
    }

    override fun getFiles(): Files {
        TODO("Not yet implemented")
    }

    override fun getClipboard(): Clipboard {
        TODO("Not yet implemented")
    }

    override fun setApplicationLogger(applicationLogger: ApplicationLogger?) {
        TODO("Not yet implemented")
    }

    override fun setLogLevel(logLevel: Int) {
        TODO("Not yet implemented")
    }

    override fun getApplicationListener(): ApplicationListener {
        TODO("Not yet implemented")
    }

    override fun removeLifecycleListener(listener: LifecycleListener?) {
        TODO("Not yet implemented")
    }

    override fun getPreferences(name: String?): Preferences {
        TODO("Not yet implemented")
    }

    override fun addLifecycleListener(listener: LifecycleListener?) {
        TODO("Not yet implemented")
    }

    override fun log(tag: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun log(tag: String?, message: String?, exception: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun getVersion(): Int {
        TODO("Not yet implemented")
    }

    override fun postRunnable(runnable: Runnable?) {
        TODO("Not yet implemented")
    }

    override fun getGraphics(): Graphics {
        TODO("Not yet implemented")
    }

    override fun getAudio(): Audio {
        TODO("Not yet implemented")
    }

    override fun getApplicationLogger(): ApplicationLogger {
        TODO("Not yet implemented")
    }

    override fun exit() {
        TODO("Not yet implemented")
    }

    override fun getType(): Application.ApplicationType {
        TODO("Not yet implemented")
    }

    override fun getInput(): Input {
        TODO("Not yet implemented")
    }

    override fun getNativeHeap(): Long {
        TODO("Not yet implemented")
    }

    override fun error(tag: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun error(tag: String?, message: String?, exception: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun getLogLevel(): Int {
        TODO("Not yet implemented")
    }

    override fun debug(tag: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun debug(tag: String?, message: String?, exception: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun getNet(): Net {
        TODO("Not yet implemented")
    }

    override fun getJavaHeap(): Long {
        TODO("Not yet implemented")
    }
}