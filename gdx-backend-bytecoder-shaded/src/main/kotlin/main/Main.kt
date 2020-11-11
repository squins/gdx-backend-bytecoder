package main

import com.squins.gdx.backends.bytecoder.BytecoderApplication
import com.squins.gdx.backends.bytecoder.BytecoderGL20
import com.mygdx.game.MyGdxGame
import com.squins.gdx.backends.bytecoder.api.web.HTMLDivElement
import com.squins.gdx.backends.bytecoder.api.web.ExtWindow
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import com.squins.gdx.backends.bytecoder.preloader.Preloader
import com.squins.gdx.backends.bytecoder.preloader.PreloaderCallback
import com.squins.gdx.backends.bytecoder.preloader.PreloaderState
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

    private fun justPreload() {
        println("justPreload called")
        val baseUrl = libgdxAppCanvas.assetBaseUrl()
        println("assetBaseUrl: $baseUrl")

        println("creating preloader")
        val preloader = Preloader(baseUrl)

        val assetFileUrl = "$baseUrl/assets.txt"

        println("calling preloader.preload()")
        preloader.preload(assetFileUrl, object : PreloaderCallback {
            override fun update(state: PreloaderState) {
                println("update not implemented")
            }

            override fun error(file: String) {
                println("error not implemented")
            }

        })
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
//            val assetNames: ObjectMap<String, String> = ObjectMap()

//            assetNames.put("badlogic-ba3e909e98a4c58c6a15f043f2e1a8a7.jpg","badlogic.jpg")
//
//            for(entry in assetNames.entries()){
//                println(entry.key + entry.value)
//            }
            println("Start in 3 2 1 go")

            // TODO: make it configurable which example to run. Dropdown choice in de HTML document?
            Main().runLibGdxExample()
        }
    }
}

