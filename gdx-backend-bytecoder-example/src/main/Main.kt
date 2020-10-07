package main

import bytecoder.BytecoderApplication
import bytecoder.BytecoderGL20
import com.badlogic.gdx.graphics.GL20
import com.mygdx.game.MyGdxGame
import de.mirkosertic.bytecoder.api.web.Window
import ext.ExtDiv
import ext.ExtWindow
import ext.LibgdxAppCanvas

class Main {
    private val window = Window.window()!! as ExtWindow
    private val document = window.document()
    val scale = window.devicePixelRatio
    private val app = (document.getElementById("app") as ExtDiv)
    private val libgdxAppCanvas = document.querySelector("#canvas1") as LibgdxAppCanvas

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
        val libGdxGl20 = BytecoderGL20(gl);

        libgdxAppCanvas.audio("bla.m4a").play();

        val cw = app.clientWidth()
        val ch = app.clientHeight()

        println("Document: ${document.title()}")

        println("retrieved gl")

        libGdxGl20.glClearColor(1.0F, 0.0F, 0.0F, 1.0F)
        libGdxGl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>?) {
            println("Start in 3 2 1 go")
            Main().runLibGdxExample()
        }
    }
}