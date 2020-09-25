package main

import com.badlogic.gdx.graphics.GL20
import com.libgdx.backends.bytecoder.BytecoderApplication
import com.libgdx.backends.bytecoder.BytecoderGL20
import com.mygdx.game.MyGdxGame
import de.mirkosertic.bytecoder.api.web.Window
import ext.*

class Main {
    val window = Window.window()!! as ExtWindow
    val document = window.document()
    val scale = window.devicePixelRatio
    val app = (document.getElementById("app") as ExtDiv)
    val canvas = document.querySelector("#canvas1") as HtmlCanvasElement
    val gl = canvas.getContext("webgl")
    val libGdxGl20 = BytecoderGL20(gl);


    init {
        app.style("float:left; width:100%; height:100%;")
    }



    private fun runLibGdxExample() {
        BytecoderApplication(MyGdxGame(), libGdxGl20)
    }

    private fun runSimpleGlExampleNoLibgdx(){

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