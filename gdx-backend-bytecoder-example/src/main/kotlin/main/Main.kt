package main

import bytecoder.BytecoderApplication
import bytecoder.BytecoderGL20
import com.badlogic.gdx.graphics.GL20
import com.mygdx.game.MyGdxGame
import de.mirkosertic.bytecoder.api.web.FloatArray
import de.mirkosertic.bytecoder.api.web.OpaqueArrays
import de.mirkosertic.bytecoder.api.web.TypedArray
import de.mirkosertic.bytecoder.api.web.Window
import ext.ExtDiv
import ext.ExtWindow
import ext.LibgdxAppCanvas
import java.nio.Buffer
import java.nio.DoubleBuffer
import java.nio.FloatBuffer

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

        println("after gl")

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

       val shaderProgram = initShaderProgram(libGdxGl20, vsSource, fsSource)

        programInfo(libGdxGl20, shaderProgram)

        libgdxAppCanvas.audio("bla.m4a").play();

        val cw = app.clientWidth()
        val ch = app.clientHeight()

        println("Document: ${document.title()}")

        println("retrieved gl")

        libGdxGl20.glClearColor(1.0F, 0.0F, 0.0F, 1.0F)
        libGdxGl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    private fun initShaderProgram(libGdxGl20: BytecoderGL20, vsSource: String, fsSource: String): Int {
        println("initShader")
        val vertexShader = loadShader(libGdxGl20, GL20.GL_VERTEX_SHADER, vsSource)
        val fragmentShader = loadShader(libGdxGl20, GL20.GL_FRAGMENT_SHADER, fsSource)

        val shaderProgram = libGdxGl20.glCreateProgram()
        libGdxGl20.glAttachShader(shaderProgram, vertexShader)
        libGdxGl20.glAttachShader(shaderProgram, fragmentShader)
        libGdxGl20.glLinkProgram(shaderProgram)

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



    data class ShaderProgrammingInfo(val program: Int,
                                     val attribLocations :AttribLocations,
                                     val uniformLocations: UniformLocations)

    data class AttribLocations(val vertexPosition : Int)

    data class UniformLocations(val projectionMatrix: Int ,
                                val modelViewMatrix: Int)

    private fun programInfo(libGdxGl20: BytecoderGL20, shaderProgram: Int): ShaderProgrammingInfo {
        return ShaderProgrammingInfo(
                program= shaderProgram,
                attribLocations = AttribLocations(libGdxGl20.glGetAttribLocation(shaderProgram, "aVertexPosition")),
                uniformLocations = UniformLocations(
                        libGdxGl20.glGetUniformLocation(shaderProgram, "uProjectionMatrix"),
                        libGdxGl20.glGetUniformLocation(shaderProgram, "uModelViewMatrix")
                )
        )
    }

    private fun initBuffers(libGdxGl20: BytecoderGL20) {
        val positionBuffer = libGdxGl20.glGenBuffer()

        libGdxGl20.glBindBuffer(GL20.GL_ARRAY_BUFFER, positionBuffer)

        //positions: -1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, -1.0

//        val positionsFloatArray : FloatArray = OpaqueArrays.createFloatArray(8)
//        positionsFloatArray.setFloat(0, -1.0F)
//        positionsFloatArray.setFloat(1, 1.0F)
//        positionsFloatArray.setFloat(2, 1.0F)
//        positionsFloatArray.setFloat(3, 1.0F)
//        positionsFloatArray.setFloat(4, -1.0F)
//        positionsFloatArray.setFloat(5, -1.0F)
//        positionsFloatArray.setFloat(6, 1.0F)
//        positionsFloatArray.setFloat(7, -1.0F)

        val floatBuffer: FloatBuffer = FloatBuffer.allocate(8)

        floatBuffer.put(-1.0F)
        floatBuffer.put(1.0F)
        floatBuffer.put(1.0F)
        floatBuffer.put(1.0F)
        floatBuffer.put(-1.0F)
        floatBuffer.put(-1.0F)
        floatBuffer.put(1.0F)
        floatBuffer.put(-1.0F)

//        val positions: Buffer = positionsFloatArray

        libGdxGl20.glBufferData(GL20.GL_ARRAY_BUFFER,  8, floatBuffer, GL20.GL_STATIC_DRAW)
    }

    private fun drawScene(libGdxGl20: BytecoderGL20, app: ExtDiv, programmingInfo: ShaderProgrammingInfo,
                          buffers: Array<Buffer>) {
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

    companion object {
        @JvmStatic
        fun main(args: Array<String>?) {
            println("Start in 3 2 1 go")
            Main().runSimpleGlExampleNoLibgdx()
        }
    }
}