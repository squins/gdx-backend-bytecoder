package main

import bytecoder.BytecoderApplication
import bytecoder.BytecoderGL20
import com.badlogic.gdx.graphics.GL20
import com.mygdx.game.MyGdxGame
import de.mirkosertic.bytecoder.api.web.HTMLDocument
import de.mirkosertic.bytecoder.api.web.Window
import ext.ExtDiv
import ext.ExtWindow
import ext.LibgdxAppCanvas
import ext.WebGLRenderingContext
import java.nio.FloatBuffer

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
        val libGdxGl20 = BytecoderGL20(gl)
        NonLibgdxSampleWebGl(app, libgdxAppCanvas, libGdxGl20,gl, document).run()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>?) {
            println("Start in 3 2 1 go")
            Main().runSimpleGlExampleNoLibgdx()
        }
    }
}

/**
 * TODO for Coen:
 * - do not use libGdxGl20, use gl instead for this example.
 * - create BytecoderGL20 by copy-pasting from GWT, TeamVM. Saves lots of time
 */
class NonLibgdxSampleWebGl(
        private val app: ExtDiv,
        private val libgdxAppCanvas: LibgdxAppCanvas,
        private val libGdxGl20: BytecoderGL20,
        val gl: WebGLRenderingContext,
        val document: HTMLDocument
) {

    fun run() {
        val vsSource = """attribute vec4 aVertexPosition;
        uniform mat4 uModelViewMatrix;
        uniform mat4 uProjectionMatrix;

        void main() {
            gl_Position = uProjectionMatrix * uModelViewMatrix * aVertexPosition;
        }"""

        val fsSource = """void main() {
            gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
        }
        """

        val shaderProgram = initShaderProgram(vsSource, fsSource)

        println("programInfo")
        val programInfo = programInfo(shaderProgram)
        println("initBuffers")
        val bufferId = initBuffers(libGdxGl20)

        println("drawScene")
        drawScene(programInfo, bufferId)

        libgdxAppCanvas.audio("bla.m4a").play();

        val cw = app.clientWidth()
        val ch = app.clientHeight()

        println("Document: ${document.title()}")

        println("retrieved gl")

        libGdxGl20.glClearColor(1.0F, 0.0F, 0.0F, 1.0F)
        libGdxGl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    data class ShaderProgrammingInfo(val program: Int,
                                     val attribLocations :AttribLocations,
                                     val uniformLocations: UniformLocations)

    data class AttribLocations(val vertexPosition : Int)

    data class UniformLocations(val projectionMatrix: Int ,
                                val modelViewMatrix: Int)

    private fun programInfo(shaderProgram: Int): ShaderProgrammingInfo {
        return ShaderProgrammingInfo(
                program= shaderProgram,
                attribLocations = AttribLocations(libGdxGl20.glGetAttribLocation(shaderProgram, "aVertexPosition")),
                uniformLocations = UniformLocations(
                        libGdxGl20.glGetUniformLocation(shaderProgram, "uProjectionMatrix"),
                        libGdxGl20.glGetUniformLocation(shaderProgram, "uModelViewMatrix")
                )
        )
    }

    private fun initBuffers(libGdxGl20: BytecoderGL20): Int {
        println("glGenBuffer")
        val positionBufferId = libGdxGl20.glGenBuffer()

        println("glBindBuffer")
        libGdxGl20.glBindBuffer(GL20.GL_ARRAY_BUFFER, positionBufferId)

        println("FloatBuffer.allocate")
        val positions: FloatBuffer = FloatBuffer.allocate(8)

        println("positions.put(floatArrayOf")
        positions.put(floatArrayOf(
                -1.0F,  1.0F,
                1.0F,  1.0F,
                -1.0F, -1.0F,
                1.0F, -1.0F
        ))

        println("glBufferData")
        libGdxGl20.glBufferData(GL20.GL_ARRAY_BUFFER,  8, positions, GL20.GL_STATIC_DRAW)

        return positionBufferId
    }

    private fun drawScene(programmingInfo: ShaderProgrammingInfo,
                          bufferId:Int) {
        val fieldOfView: Double = 45 * Math.PI / 180
        val aspect = app.clientWidth() / app.clientHeight();
        val zNear : Double = 0.1
        val zFar : Double = 100.0

        /*const projectionMatrix = mat4.create();

        // note: glmatrix.js always has the first argument
        // as the destination to receive the result.
        mat4.perspective(projectionMatrix,
                fieldOfView,
                aspect,
                zNear,
                zFar);

        // Set the drawing position to the "identity" point, which is
        // the center of the scene.
        const modelViewMatrix = mat4.create();

        // Now move the drawing position a bit to where we want to
        // start drawing the square.

        mat4.translate(modelViewMatrix,     // destination matrix
                modelViewMatrix,     // matrix to translate
                [-0.0, 0.0, -6.0]);  // amount to translate

        // Tell WebGL how to pull out the positions from the position
        // buffer into the vertexPosition attribute.
        {*/

        val numComponents : Int = 2;  // pull out 2 values per iteration
        val type : Int = GL20.GL_FLOAT;    // the data in the buffer is 32bit floats
        val normalize : Boolean = false;  // don't normalize
        val stride : Int = 0;         // how many bytes to get from one set of values to the next
        // 0 = use type and numComponents above
        val offset : Int = 0;         // how many bytes inside the buffer to start from
        //libGdxGl20.glBindBuffer(GL20.GL_ARRAY_BUFFER, buffers.position);

        libGdxGl20.glVertexAttribPointer(
                programmingInfo.attribLocations.vertexPosition,
                numComponents,
                type,
                normalize,
                stride,
                offset);
        libGdxGl20.glEnableVertexAttribArray(
                programmingInfo.attribLocations.vertexPosition);
    }


    private fun initShaderProgram(vsSource: String, fsSource: String): Int {
        println("initShader vertexShader")
        val vertexShader = loadShader(libGdxGl20, GL20.GL_VERTEX_SHADER, vsSource)
        println("initShader fragmentShader")
        val fragmentShader = loadShader(libGdxGl20, GL20.GL_FRAGMENT_SHADER, fsSource)

        println("glCreateProgram")
        val shaderProgram = libGdxGl20.glCreateProgram()
        println("glAttachShader vertexShader")
        libGdxGl20.glAttachShader(shaderProgram, vertexShader)
        println("glAttachShader fragmentShader")
        libGdxGl20.glAttachShader(shaderProgram, fragmentShader)
        println("shaderProgram")
        libGdxGl20.glLinkProgram(shaderProgram)
        println("return shaderProgram")

//        if (!libGdxGl20.getProgramParameter(shaderProgram, GL20.GL_LINK_STATUS)) {
//            println('Unable to initialize the shader program: ' + libGdxGl20.glGetProgramInfoLog(shaderProgram));
//            return null;
//        }

        return shaderProgram
    }

    private fun loadShader(libGdxGl20: BytecoderGL20, type: Int, source: String): Int {
        val shader = libGdxGl20.glCreateShader(type)

        libGdxGl20.glShaderSource(shader, source)

        libGdxGl20.glCompileShader(shader)

        return shader
    }

}