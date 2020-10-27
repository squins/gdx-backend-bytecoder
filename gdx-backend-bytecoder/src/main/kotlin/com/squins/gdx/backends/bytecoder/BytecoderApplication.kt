package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.*
import com.badlogic.gdx.utils.Clipboard
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import com.squins.gdx.backends.bytecoder.preloader.Preloader
import com.squins.gdx.backends.bytecoder.preloader.Preloader.PreloaderCallback
import com.squins.gdx.backends.bytecoder.preloader.Preloader.PreloaderState


class BytecoderApplication(val listener: ApplicationListener, val libgdxAppCanvas: LibgdxAppCanvas) : Application {
    var preloader = Preloader()

    init {

        println("Init")
        val gl = libgdxAppCanvas.getContext("webgl")
        val bytecoderGL20 = BytecoderGL20(gl);
        val graphics = BytecoderGraphics(libgdxAppCanvas)

        println("Init app")
        Gdx.app = this
        println("Init gl")
        Gdx.gl = bytecoderGL20
        println("Init gl20")
        Gdx.gl20 = bytecoderGL20
        println("Init audio")
        Gdx.audio = BytecoderAudio(libgdxAppCanvas)
        println("Before Gdx.files")
        Gdx.files = BytecoderFiles()
        println("Before Gdx.graphics")
        Gdx.graphics = graphics

        print("Before preload")
        preloader.preload("libgdx-sample-app/core/assets")
        print("After preload")

        listener.create()
        println("created")
        listener.render()
        println("rendered")
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

    fun preloadAssets() {
        val callback: PreloaderCallback = getPreloaderCallback()
        preloader = createPreloader()
        preloader.preload("assets.txt", object: PreloaderCallback {
            override fun error(file: String) {
                callback.error(file)
            }

            override fun update(state: PreloaderState) {
                callback.update(state)
                if (state.hasEnded()) {
                    getRootPanel().clear()
                    if (loadingListener != null) loadingListener.beforeSetup()
                    setupLoop()
                    addEventListeners()
                    if (loadingListener != null) loadingListener.afterSetup()
                }
            }
        })
    }

    fun getPreloaderBaseURL(): String? {
        return GWT.getHostPageBaseURL().toString() + "assets/"
    }

    fun createPreloader(): Preloader {
        return Preloader(getPreloaderBaseURL())
    }

    fun getPreloaderCallback(): PreloaderCallback {
        return createPreloaderPanel(Gdx.getModuleBaseURL().toString() + "logo.png")
    }

    protected fun createPreloaderPanel(logoUrl: String?): PreloaderCallback {
        val preloaderPanel: Panel = VerticalPanel()
        preloaderPanel.setStyleName("gdx-preloader")
        val logo = Image(logoUrl)
        logo.setStyleName("logo")
        preloaderPanel.add(logo)
        val meterPanel: Panel = SimplePanel()
        val meter = InlineHTML()
        val meterStyle: Style = meter.getElement().getStyle()
        meterStyle.setWidth(0, Unit.PCT)
        adjustMeterPanel(meterPanel, meterStyle)
        meterPanel.add(meter)
        preloaderPanel.add(meterPanel)
        getRootPanel().add(preloaderPanel)
        return object : PreloaderCallback {
            override fun error(file: String) {
                println("error: $file")
            }

            override fun update(state: PreloaderState) {
                meterStyle.setWidth(100f * state.progress, Unit.PCT)
            }
        }
    }


}