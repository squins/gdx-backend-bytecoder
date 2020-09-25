package main

import com.badlogic.gdx.graphics.GL20
import com.libgdx.backends.bytecoder.BytecoderGL20
import de.mirkosertic.bytecoder.api.web.Window
import ext.*

class Main {

    private fun initGL(){
        val window = Window.window()!! as ExtWindow
        val document = window.document()
        val scale = window.devicePixelRatio
        val app = (document.getElementById("app") as ExtDiv)

        app.style("float:left; width:100%; height:100%;")
        val cw = app.clientWidth()
        val ch = app.clientHeight()

        println("Document: ${document.title()}")

        val canvas = document.querySelector("#canvas1") as HtmlCanvasElement

        val gl = canvas.getContext("webgl") as WebGLRenderingContext
        println("retrieved gl")

        val gl20 = BytecoderGL20(gl);

        gl20.glClearColor(1.0F, 0.0F, 0.0F, 1.0F)
        gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>?) {
            println("Start in 3 2 1 go")
            Main().initGL()
        }
    }
}