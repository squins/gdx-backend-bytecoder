package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.*
import com.badlogic.gdx.utils.Clipboard
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import com.squins.gdx.backends.bytecoder.preloader.AssetFilter
import com.squins.gdx.backends.bytecoder.preloader.Preloader
import com.squins.gdx.backends.bytecoder.preloader.Preloader.PreloaderCallback
import com.squins.gdx.backends.bytecoder.preloader.Preloader.PreloaderState
import de.mirkosertic.bytecoder.api.web.AnimationFrameCallback
import de.mirkosertic.bytecoder.api.web.Window


class BytecoderApplication(val listener: ApplicationListener,
                           val libgdxAppCanvas: LibgdxAppCanvas) : Application {

    val preloader:Preloader

    private var logLevel:Int = Application.LOG_INFO

    private val assetBaseUrl = libgdxAppCanvas.assetBaseUrl()

    init {
        println("Init")

        Gdx.app = this
        preloader = Preloader(assetBaseUrl)
        val gl = libgdxAppCanvas.getContext("webgl")
        val bytecoderGL20 = BytecoderGL20(gl)
        Gdx.graphics =  BytecoderGraphics(libgdxAppCanvas)

        println("Init app")
        println("Init gl")
        Gdx.gl = bytecoderGL20
        println("Init gl20")
        Gdx.gl20 = bytecoderGL20
        println("Init audio")
        Gdx.audio = BytecoderAudio(libgdxAppCanvas)
        println("Before Gdx.files")
        Gdx.files = BytecoderFiles(preloader)
        println("Before Gdx.graphics")
        println("Nogmaals: Before gdx.graphics, is null?")
        println("Before preload")
//        preloader.preload("libgdx-sample-app/core/assets")
        val assets = listOf(
                Preloader.Asset("badlogic.jpg", "badlogic.jpg", AssetFilter.AssetType.Image, 0L, "image/jpeg")
        )
        println("Created assets list")

        preloader.doLoadAssets(assets, object : PreloaderCallback {
            override fun update(state: PreloaderState) {
                println("preloader.doLoadAssets.update called, state: $state, size: ${preloader.images.size} ")
                if (preloader.images.size > 0) {
                    println("preloader.doLoadAssets hasEnded!")
                    listener.create()
                    println("created")

                    // TODO move render to loop with requestAnimationFrame

                    Window.window().requestAnimationFrame(object:AnimationFrameCallback {
                        override fun run(aElapsedTime: Int) {
                            println("Before render")
                            listener.render()
                            println("rendered")

                        }
                    })
                }
            }

            override fun error(file: String) {
                println("preloader.doLoadAssets.error called: $file")
            }
        })
        println("After preload")
    }

    override fun setLogLevel(logLevel: Int) {
        this.logLevel = logLevel
    }

    override fun getLogLevel(): Int {
        return logLevel
    }

    override fun getFiles(): Files {
        return Gdx.files
    }

    override fun getClipboard(): Clipboard {
        TODO("Not yet implemented")
    }

    override fun setApplicationLogger(applicationLogger: ApplicationLogger) {
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
        return Gdx.graphics
    }

    override fun getAudio(): Audio {
        return Gdx.audio
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

    override fun error(tag: String, message: String) {
        System.err.println("[$tag] $message")
    }

    override fun error(tag: String, message: String, exception: Throwable) {
        System.err.println("[$tag] $message")
        exception.printStackTrace(System.err)
    }

    override fun debug(tag: String, message: String) {
        println("[$tag] $message")
    }

    override fun debug(tag: String, message: String, exception: Throwable) {
        println("[$tag] $message")
        exception.printStackTrace(System.out)
    }

    override fun getNet(): Net {
        TODO("Not yet implemented")
    }

    override fun getJavaHeap(): Long {
        TODO("Not yet implemented")
    }

    fun preloadAssets() {
        val callback: PreloaderCallback = getPreloaderCallback()
        preloader.preload("assets.txt", object: PreloaderCallback {
            override fun error(file: String) {
                callback.error(file)
            }

            override fun update(state: PreloaderState) {
                callback.update(state)
                if (state.hasEnded()) {
//                    getRootPanel().clear()
//                    if (loadingListener != null) loadingListener.beforeSetup()
//                    setupLoop() // TODO: we need this too
//                    addEventListeners()
//                    if (loadingListener != null) loadingListener.afterSetup()
                }
            }
        })
    }

    private fun getPreloaderCallback(): PreloaderCallback {
        return createPreloaderPanel( "$assetBaseUrl/logo.png")
    }

    private fun createPreloaderPanel(logoUrl: String): PreloaderCallback {
//        val preloaderPanel: Panel = VerticalPanel()
//        preloaderPanel.setStyleName("gdx-preloader")
//        val logo = Image(logoUrl)
//        logo.setStyleName("logo")
//        preloaderPanel.add(logo)
//        val meterPanel: Panel = SimplePanel()
//        val meter = InlineHTML()
//        val meterStyle: Style = meter.getElement().getStyle()
//        meterStyle.setWidth(0, Unit.PCT)
//        adjustMeterPanel(meterPanel, meterStyle)
//        meterPanel.add(meter)
//        preloaderPanel.add(meterPanel)
//        getRootPanel().add(preloaderPanel)
        return object : PreloaderCallback {
            override fun error(file: String) {
                println("error: $file")
            }

            override fun update(state: PreloaderState) {
//                meterStyle.setWidth(100f * state.progress, Unit.PCT)
            }
        }
    }


}