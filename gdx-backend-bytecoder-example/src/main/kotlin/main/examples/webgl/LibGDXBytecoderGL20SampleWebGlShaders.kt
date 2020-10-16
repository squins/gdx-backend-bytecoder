package main.examples.webgl

import bytecoder.BytecoderGL20
import com.badlogic.gdx.graphics.GL20
import de.mirkosertic.bytecoder.api.web.HTMLDocument
import de.mirkosertic.bytecoder.api.web.OpaqueArrays
import ext.*
import java.nio.Buffer
import java.nio.FloatBuffer

class LibGDXBytecoderGL20SampleWebGlShaders(
        private val app: ExtDiv,
        private val libgdxAppCanvas: LibgdxAppCanvas,
        private val libGdxGl20: BytecoderGL20,
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

        println("retrieved gl")
    }

    data class ShaderProgrammingInfo(val program: Int,
                                     val attribLocations : AttribLocations,
                                     val uniformLocations: UniformLocations)

    data class AttribLocations(val vertexPosition : Int)

    data class UniformLocations(val projectionMatrix: Int,
                                val modelViewMatrix: Int)

    private fun programInfo(shaderProgram: Int): ShaderProgrammingInfo {
        return ShaderProgrammingInfo(
                program = shaderProgram,
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
        val positionsBuffer: FloatBuffer = FloatBuffer.allocate(8)

        println("positions.put(floatArrayOf")
        val positionsKotlinArray = floatArrayOf(
                -1.0F, 1.0F,
                1.0F, 1.0F,
                -1.0F, -1.0F,
                1.0F, -1.0F
        )
        positionsBuffer.put(positionsKotlinArray)

        val bytecoderArray = OpaqueArrays.createFloatArray(positionsKotlinArray.size)

        positionsKotlinArray.forEachIndexed{index,value -> bytecoderArray.setFloat(index, value)}

        libGdxGl20.glBufferData(GL20.GL_ARRAY_BUFFER,  8, positionsBuffer, GL20.GL_STATIC_DRAW)

        println("glBufferData")
        return positionBufferId
    }

    private fun drawScene(programmingInfo: ShaderProgrammingInfo,
                          bufferId: Int) {
        val mat4 : WebMat4 = libgdxAppCanvas.mat4()

        libGdxGl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f) // Clear to black, fully opaque
        libGdxGl20.glClearDepthf(1.0f) // Clear everything
        libGdxGl20.glEnable(GL20.GL_DEPTH_TEST) // Enable depth testing
        libGdxGl20.glDepthFunc(GL20.GL_LEQUAL) // Near things obscure far things

        // Clear the canvas before we start drawing on it.
        libGdxGl20.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        val fieldOfView: Float = (45 * Math.PI / 180).toFloat()
        val aspect = app.clientWidth() / app.clientHeight()
        val zNear = 0.1F
        val zFar = 100.0F

        val projectionMatrix = mat4.create()

        mat4.perspective(projectionMatrix,
                fieldOfView,
                aspect,
                zNear,
                zFar)

        // Set the drawing position to the "identity" point, which is
        // the center of the scene.
        val modelViewMatrix = mat4.create()

        // Now move the drawing position a bit to where we want to
        // start drawing the square.

        mat4.translate(modelViewMatrix,                                 // destination matrix
                modelViewMatrix,                                        // matrix to translate
                convertToBytecoder(floatArrayOf(-0.0F, 0.0F, -6.0F)))   // amount to translate

        // Tell WebGL how to pull out the positions from the position
        // buffer into the vertexPosition attribute.

        val numComponents = 2               // pull out 2 values per iteration
        val type : Int = GL20.GL_FLOAT      // the data in the buffer is 32bit floats
        val normalize = false               // don't normalize
        val stride = 0                      // how many bytes to get from one set of values to the next
        val offset = 0                      // how many bytes inside the buffer to start from

        libGdxGl20.glBindBuffer(GL20.GL_ARRAY_BUFFER, bufferId)

        libGdxGl20.glVertexAttribPointer(
                programmingInfo.attribLocations.vertexPosition,
                numComponents,
                type,
                normalize,
                stride,
                offset)
        libGdxGl20.glEnableVertexAttribArray(
                programmingInfo.attribLocations.vertexPosition)

        libGdxGl20.glUseProgram(programmingInfo.program)
//        libGdxGl20.glUniformMatrix4fv(
//                programmingInfo.uniformLocations.projectionMatrix,
//                0,
//                false,
//                projectionMatrix)
//        libGdxGl20.glUniformMatrix4fv(
//                programmingInfo.uniformLocations.modelViewMatrix,
//                0,
//                false,
//                modelViewMatrix)

        val vertexCount = 4
        libGdxGl20.glDrawArrays(GL20.GL_TRIANGLE_STRIP, offset, vertexCount)
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
        return shaderProgram
    }

    private fun loadShader(libGdxGl20: BytecoderGL20, type: Int, source: String): Int {
        val shader = libGdxGl20.glCreateShader(type)

        libGdxGl20.glShaderSource(shader, source)

        libGdxGl20.glCompileShader(shader)

        return shader
    }

    private fun convertToBytecoder(positionsKotlinArray: FloatArray): de.mirkosertic.bytecoder.api.web.FloatArray {
        val bytecoderArray = OpaqueArrays.createFloatArray(positionsKotlinArray.size)

        positionsKotlinArray.forEachIndexed { index, value -> bytecoderArray.setFloat(index, value) }
        return bytecoderArray
    }
}