package main

import bytecoder.BytecoderApplication
import bytecoder.BytecoderGL20
import com.mygdx.game.MyGdxGame
import de.mirkosertic.bytecoder.api.web.Window
import ext.*
import main.examples.audio.BytecoderSampleAudio
import main.examples.webgl.BytecoderSampleWebGlShaders

class Main {
    private val window = Window.window()!! as ExtWindow
    private val document = window.document()
    val scale = window.devicePixelRatio
    private val app = (document.getElementById("app") as ExtDiv)
    private val libgdxAppCanvas = document.querySelector("#canvas1") as LibgdxAppCanvas

    // TODO: move this to external class, only used when running runSimpleGlExampleNoLibgdx

    init {
        app.style("float:left; width:100%; height:100%;")
    }

    private fun runLibGdxExample() {
        println("runLibGdxExample")
        BytecoderApplication(MyGdxGame(), libgdxAppCanvas)
    }

    private fun runSimpleGlExampleNoLibgdx(){
        println("runSimpleGlExampleNoLibgdx")
        val gl = libgdxAppCanvas.getContext("webgl")
        //BytecoderSampleWebGlShaders(app, libgdxAppCanvas, gl).run()
        BytecoderSampleAudio(libgdxAppCanvas).run()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>?) {
            println("Start in 3 2 1 go")
            Main().runSimpleGlExampleNoLibgdx()
        }
    }
}

