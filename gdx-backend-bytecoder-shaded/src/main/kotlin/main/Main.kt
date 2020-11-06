package main

import com.squins.gdx.backends.bytecoder.BytecoderApplication
import com.squins.gdx.backends.bytecoder.BytecoderGL20
import com.mygdx.game.MyGdxGame
import com.squins.gdx.backends.bytecoder.api.web.HTMLDivElement
import com.squins.gdx.backends.bytecoder.api.web.ExtWindow
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import de.mirkosertic.bytecoder.api.web.Window
import main.examples.webgl.LibGDXBytecoderGL20SampleWebGlShaders

class Main {
    private val window = Window.window()!! as ExtWindow
    private val document = window.document()
    val scale = window.devicePixelRatio
    private val app = (document.getElementById("app") as HTMLDivElement)
    private val libgdxAppCanvas = document.querySelector("#canvas1") as LibgdxAppCanvas

    // TODO: move this to external class, only used when running runSimpleGlExampleNoLibgdx

    init {
        app.style("float:left; width:100%; height:100%;")
    }

    private fun runLibGdxExample() {
        println("runLibGdxExample")
        BytecoderApplication(MyGdxGame(), libgdxAppCanvas)
    }

    private fun runSimpleGlExampleSimpleApp(){
        println("runSimpleGlExampleNoLibgdx")
        val gl = libgdxAppCanvas.getContext("webgl")
//        BytecoderSampleWebGlShaders(app, libgdxAppCanvas, gl).run()
//        BytecoderSampleAudio(libgdxAppCanvas).run()
        LibGDXBytecoderGL20SampleWebGlShaders(app, libgdxAppCanvas, BytecoderGL20(gl)).run()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>?) {
            println("Start in 3 2 1 go")

            // TODO: make it configurable which exampel to run. Dropdown choice in de HTML document?
            Main().runLibGdxExample()
        }
    }
}

