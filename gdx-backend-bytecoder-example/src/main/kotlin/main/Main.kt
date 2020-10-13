package main

import bytecoder.BytecoderApplication
import bytecoder.BytecoderGL20
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.BufferUtils
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
        val libGdxGl20 = BytecoderGL20(gl)


        // constructor ShaderProgram, compile does this all

        println("glCreateShader")
        val shader = libGdxGl20.glCreateShader(GL20.GL_VERTEX_SHADER)

        val source = """attribute vec4 ${ShaderProgram.POSITION_ATTRIBUTE};
attribute vec4 ${ShaderProgram.COLOR_ATTRIBUTE};
attribute vec2 ${ShaderProgram.TEXCOORD_ATTRIBUTE}0;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;

void main()
{
   v_color = ${ShaderProgram.COLOR_ATTRIBUTE};
   v_color.a = v_color.a * (255.0/254.0);
   v_texCoords = ${ShaderProgram.TEXCOORD_ATTRIBUTE}0;
   gl_Position =  u_projTrans * ${ShaderProgram.POSITION_ATTRIBUTE};
}
"""

        println("before glShaderSource")
        libGdxGl20.glShaderSource(shader, source)

        println("before glCompileShader")
        libGdxGl20.glCompileShader(shader)

        println("before BufferUtils.newIntBuffer")
        val intbuf = BufferUtils.newIntBuffer(1)

        libGdxGl20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, intbuf)



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
            Main().runSimpleGlExampleNoLibgdx()
        }
    }
}