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

    val preloader: Preloader
    val graphics: BytecoderGraphics
    val files: BytecoderFiles
    val audio: BytecoderAudio
    private var lastWidth: Int = 0
    private var lastHeight: Int = 0
    private var logLevel:Int = Application.LOG_INFO
    private val assetBaseUrl = libgdxAppCanvas.assetBaseUrl()

    init {
        println("Init")

        Gdx.app = this
        preloader = Preloader(assetBaseUrl)
        val gl = libgdxAppCanvas.getContext("webgl")
        val bytecoderGL20 = BytecoderGL20(gl)
        graphics = BytecoderGraphics(libgdxAppCanvas)
        files = BytecoderFiles(preloader)
        audio = BytecoderAudio(libgdxAppCanvas)

        println("Init gl")
        Gdx.gl = bytecoderGL20
        println("Init gl20")
        Gdx.gl20 = bytecoderGL20
        println("Init audio")
        Gdx.audio = BytecoderAudio(libgdxAppCanvas)
        println("Before Gdx.files")
        Gdx.files = files
        println("Before Gdx.graphics")
        Gdx.graphics = graphics
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

    fun setupLoop(){
        val gl = libgdxAppCanvas.getContext("webgl")
        val bytecoderGL20 = BytecoderGL20(gl)
        Gdx.app = this
        Gdx.graphics = graphics
        Gdx.gl20 = bytecoderGL20
        Gdx.gl = bytecoderGL20
        Gdx.audio = BytecoderAudio(libgdxAppCanvas)
        Gdx.files = BytecoderFiles(preloader)
        lastWidth = graphics.width
        lastHeight = graphics.height

        try {
            listener.create()
            listener.resize(graphics.width, graphics.height)
        } catch (t : Throwable){
            error("BytecoderApplication", "exception: " + t.message, t)
            t.printStackTrace()
            throw RuntimeException(t)
        }

        Window.window().requestAnimationFrame(object:AnimationFrameCallback {
            override fun run(aElapsedTime: Int) {
                try {
                    mainLoop()
                } catch (t : Throwable){
                    error("BytecoderApplication", "exception: " + t.message, t)
                    throw RuntimeException(t)
                }

            }

        })
    }

    fun mainLoop(){
        graphics.update()
        if (Gdx.graphics.width !== lastWidth || Gdx.graphics.height !== lastHeight) {
            lastWidth = graphics.width
            lastHeight = graphics.height
            Gdx.gl.glViewport(0, 0, lastWidth, lastHeight)
            this.listener.resize(lastWidth, lastHeight)
        }
//        runnablesHelper.addAll(runnables)
//        runnables.clear()
//        for (i in 0 until runnablesHelper.size) {
//            runnablesHelper.get(i).run()
//        }
//        runnablesHelper.clear()
        graphics.frameId++
        listener.render()
//        input.reset()
    }

    override fun setLogLevel(logLevel: Int) {
        this.logLevel = logLevel
    }

    override fun getLogLevel(): Int {
        return logLevel
    }

    override fun getFiles(): Files {
        return files
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
        return graphics
    }

    override fun getAudio(): Audio {
        return audio
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