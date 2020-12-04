package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.*
import com.badlogic.gdx.utils.Clipboard
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import com.squins.gdx.backends.bytecoder.preloader.Preloader
import com.squins.gdx.backends.bytecoder.preloader.PreloaderCallback
import com.squins.gdx.backends.bytecoder.preloader.PreloaderState
import de.mirkosertic.bytecoder.api.web.AnimationFrameCallback
import de.mirkosertic.bytecoder.api.web.Window


class BytecoderApplication(private var listener: ApplicationListener,
                           val libgdxAppCanvas: LibgdxAppCanvas,
                           config: BytecoderApplicationConfiguration = BytecoderApplicationConfiguration()) : Application {

    private val assetBaseUrl = libgdxAppCanvas.assetBaseUrl()
    val preloader: Preloader
    val graphics: BytecoderGraphics
    val files: BytecoderFiles
    val audio: BytecoderAudio
    private var lastWidth: Int = 0
    private var lastHeight: Int = 0
    private var logLevel:Int = Application.LOG_INFO
    private var applicationLogger : ApplicationLogger = BytecoderApplicationLogger()
    private var input:BytecoderInput = BytecoderInput(libgdxAppCanvas, config)
    private val runnables = mutableListOf<Runnable>()
    private val runnablesHelper = mutableListOf<Runnable>()
    private val lifecycleListeners = mutableListOf<LifecycleListener>()

    init {
        // DISABLED: performance println("Init")

        Gdx.app = this
        preloader = Preloader(assetBaseUrl)
        val gl = libgdxAppCanvas.getContext("webgl")
        val bytecoderGL20 = BytecoderGL20(gl)
        graphics = BytecoderGraphics(libgdxAppCanvas)
        files = BytecoderFiles(preloader)
        audio = BytecoderAudio(libgdxAppCanvas)

        // DISABLED: performance println("Init gl")
        Gdx.gl = bytecoderGL20
        // DISABLED: performance println("Init gl20")
        Gdx.gl20 = bytecoderGL20
        // DISABLED: performance println("Init audio")
        Gdx.audio = audio
        // DISABLED: performance println("Before Gdx.files")
        Gdx.files = files
        // DISABLED: performance println("Before Gdx.graphics")
        Gdx.graphics = graphics

        // DISABLED: performance println("Calling preloadAssets()")
        preloadAssets()
    }


    fun setupLoop(){
        println("setupLoop()!!!!!!!!!!!!!!")
        try {
            listener.create()
            listener.resize(graphics.width, graphics.height)
        } catch (t: Throwable){
            error("BytecoderApplication", "exception: " + t.message, t)
            t.printStackTrace()
            throw RuntimeException(t)
        }

        requestAnimationFrame()
    }

    private fun requestAnimationFrame() {
        Window.window().requestAnimationFrame(object : AnimationFrameCallback {
            override fun run(aElapsedTime: Int) {
                mainLoop()
            }
        })
    }

    fun mainLoop(){
        // DISABLED: performance println("mainLoop")
        graphics.update()
        if (Gdx.graphics.width != lastWidth || Gdx.graphics.height != lastHeight) {
            println("Gdx.graphics.width != lastWidth || Gdx.graphics.height != lastHeight")
            lastWidth = graphics.width
            lastHeight = graphics.height
            Gdx.gl.glViewport(0, 0, lastWidth, lastHeight)
            this.listener.resize(lastWidth, lastHeight)
        }
        runnablesHelper.addAll(runnables)
        runnables.clear()
        for (i in 0 until runnablesHelper.size) {
            runnablesHelper[i].run()
        }
        runnablesHelper.clear()
        graphics.frameId++
        listener.render()
        requestAnimationFrame()
    }

    override fun setLogLevel(logLevel: Int) {
        this.logLevel = logLevel
    }

    override fun getLogLevel(): Int {
        return logLevel
    }

    override fun getFiles(): Files = files

    override fun getClipboard(): Clipboard {
        return BytecoderClipboard()
    }

    override fun setApplicationLogger(applicationLogger: ApplicationLogger) {
        this.applicationLogger = applicationLogger
    }

    override fun getApplicationListener(): ApplicationListener {
        return listener
    }

    override fun removeLifecycleListener(listener: LifecycleListener) {
        lifecycleListeners.remove(listener)
    }

    override fun getPreferences(name: String): Preferences {
        TODO("Not yet implemented")
    }

    override fun addLifecycleListener(listener: LifecycleListener) {
        lifecycleListeners += listener
    }

    override fun log(tag: String, message: String) {
        println("[$tag] $message")
    }

    override fun log(tag: String, message: String, exception: Throwable) {
        println("[$tag] $message")
        exception.printStackTrace(System.out)
    }

    override fun getVersion(): Int {
        return 0
    }

    override fun postRunnable(runnable: Runnable) {
        throw makeAndLogIllegalArgumentException("BytecoderApplication.postRunnable", "Not implemented")
    }

    override fun getGraphics(): Graphics {
        return graphics
    }

    override fun getAudio(): Audio {
        return audio
    }

    override fun getApplicationLogger(): ApplicationLogger {
        return applicationLogger
    }

    override fun exit() {
        println("exit() not allowed in webapp")
    }

    override fun getType(): Application.ApplicationType {
        return Application.ApplicationType.WebGL
    }

    override fun getInput(): Input {
        return input
    }

    override fun getNativeHeap(): Long {
        return 1L
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
        throw makeAndLogIllegalArgumentException("getNet()", "not implemented")
    }

    override fun getJavaHeap(): Long {
        throw makeAndLogIllegalArgumentException("getJavaHeap()", "Not yet implemented")
    }

    private fun preloadAssets() {
        // DISABLED: performance println("("preloadAssets")
        val logoPreloaderCallback: PreloaderCallback = preloaderPanelCallbackWithLogo()
        // DISABLED: performance println("("PreloaderCallback.getPreloaderCallback created, creating assetFileUrl")
        val assetFileUrl = "$assetBaseUrl/assets.txt"

        preloader.preload(assetFileUrl, object : PreloaderCallback {
            override fun error(file: String) {
                // DISABLED: performance println("("file $file")
                logoPreloaderCallback.error(file)
            }

            override fun update(state: PreloaderState) {
                logoPreloaderCallback.update(state)
                if (state.hasEnded()) {
                    println("hasEnded()")
//                    getRootPanel().clear()
                    setupLoop()
//                    addEventListeners()
                }
            }
        })
        // DISABLED: performance println("("Created delegating callback")
        // DISABLED: performance println("("preloader.preload: $assetFileUrl")
    }

    private fun preloaderPanelCallbackWithLogo(): PreloaderCallback {
        // DISABLED: performance println("("getPreloaderCallback")
        return createPreloaderPanel("$assetBaseUrl/logo.png")
    }

    private fun createPreloaderPanel(logoUrl: String): PreloaderCallback {
        // DISABLED: performance println("("createPreloaderPanel")
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
                println("update PreloaderState")
//                meterStyle.setWidth(100f * state.progress, Unit.PCT)
            }
        }
    }

    /**
     * LoadingListener interface main purpose is to do some things before or after [BytecoderApplication.setupLoop]
     */
    interface LoadingListener {
        /**
         * Method called before the setup
         */
        fun beforeSetup()

        /**
         * Method called after the setup
         */
        fun afterSetup()
    }


}

