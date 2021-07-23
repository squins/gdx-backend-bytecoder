package main.examples.webgl

import com.badlogic.gdx.graphics.GL20
import com.squins.gdx.backends.bytecoder.api.web.*
import de.mirkosertic.bytecoder.api.web.OpaqueArrays
import de.mirkosertic.bytecoder.api.web.webgl.*
import java.nio.FloatBuffer

@Suppress("SameParameterValue", "SameParameterValue")
class BytecoderSampleWebGlShaders(
        private val app: HTMLDivElement,
        private val libgdxAppCanvas: LibgdxAppCanvas,
        private val gl: WebGLRenderingContext
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

        // DISABLED: performance // DISABLED: performance println("programInfo")
        val programInfo = programInfo(shaderProgram)

        // DISABLED: performance // DISABLED: performance println("initBuffers")
        val bufferId = initBuffers(gl)

        // DISABLED: performance // DISABLED: performance println("drawScene")
        drawScene(programInfo, bufferId)

        // DISABLED: performance // DISABLED: performance println("retrieved gl")
    }

    data class ShaderProgrammingInfo(val program: WebGLProgram,
                                     val attribLocations : AttribLocations,
                                     val uniformLocations: UniformLocations)

    data class AttribLocations(val vertexPosition : Int)

    data class UniformLocations(val projectionMatrix: WebGLUniformLocation,
                                val modelViewMatrix: WebGLUniformLocation)

    private fun programInfo(shaderProgram: WebGLProgram): ShaderProgrammingInfo {
        return ShaderProgrammingInfo(
                program = shaderProgram,
                attribLocations = AttribLocations(gl.getAttribLocation(shaderProgram, "aVertexPosition")),
                uniformLocations = UniformLocations(
                        gl.getUniformLocation(shaderProgram, "uProjectionMatrix"),
                        gl.getUniformLocation(shaderProgram, "uModelViewMatrix")
                )
        )
    }

    private fun initBuffers(gl: WebGLRenderingContext): WebGLBuffer {
        // DISABLED: performance // DISABLED: performance println("glGenBuffer")
        val positionBufferId = gl.createBuffer()

        // DISABLED: performance // DISABLED: performance println("glBindBuffer")
        gl.bindBuffer(GL20.GL_ARRAY_BUFFER, positionBufferId)

        // DISABLED: performance // DISABLED: performance println("FloatBuffer.allocate")
        val positionsBuffer: FloatBuffer = FloatBuffer.allocate(8)

        // DISABLED: performance // DISABLED: performance println("positions.put(floatArrayOf")
        val positionsKotlinArray = floatArrayOf(
                -1.0F, 1.0F,
                1.0F, 1.0F,
                -1.0F, -1.0F,
                1.0F, -1.0F
        )
        positionsBuffer.put(positionsKotlinArray)

        val bytecoderArray = convertToBytecoder(positionsKotlinArray)

        gl.bufferData(GL20.GL_ARRAY_BUFFER, bytecoderArray, GL20.GL_STATIC_DRAW)

        // DISABLED: performance println("glBufferData")
        return positionBufferId
    }

    private fun convertToBytecoder(positionsKotlinArray: FloatArray): de.mirkosertic.bytecoder.api.web.FloatArray {
        val bytecoderArray = OpaqueArrays.createFloatArray(positionsKotlinArray.size)

        positionsKotlinArray.forEachIndexed { index, value -> bytecoderArray.setFloat(index, value) }
        return bytecoderArray
    }

    private fun drawScene(programmingInfo: ShaderProgrammingInfo,
                          bufferId: WebGLBuffer) {
        val mat4 : WebMat4 = libgdxAppCanvas.mat4()

        gl.clearColor(0.0f, 0.0f, 0.0f, 1.0f) // Clear to black, fully opaque
        gl.clearDepth(1.0f) // Clear everything
        gl.enable(GL20.GL_DEPTH_TEST) // Enable depth testing
        gl.depthFunc(GL20.GL_LEQUAL) // Near things obscure far things

        // Clear the canvas before we start drawing on it.
        gl.clear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)


        val fieldOfView: Float = (45 * Math.PI / 180).toFloat()
        val aspect: Float = (app.clientWidth() / app.clientHeight()).toFloat()
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

        gl.bindBuffer(GL20.GL_ARRAY_BUFFER, bufferId)

        gl.vertexAttribPointer(
                programmingInfo.attribLocations.vertexPosition,
                numComponents,
                type,
                normalize,
                stride,
                offset)
        gl.enableVertexAttribArray(
                programmingInfo.attribLocations.vertexPosition)

        gl.useProgram(programmingInfo.program)

        gl.uniformMatrix4fv(
                programmingInfo.uniformLocations.projectionMatrix,
                false,
                projectionMatrix)
        gl.uniformMatrix4fv(
                programmingInfo.uniformLocations.modelViewMatrix,
                false,
                modelViewMatrix)

        val vertexCount = 4
        gl.drawArrays(GL20.GL_TRIANGLE_STRIP, offset, vertexCount)

    }


    private fun initShaderProgram(vsSource: String, fsSource: String): WebGLProgram {
        // DISABLED: performance println("initShader vertexShader")
        val vertexShader = loadShader(gl, GL20.GL_VERTEX_SHADER, vsSource)

        // DISABLED: performance println("initShader fragmentShader")
        val fragmentShader = loadShader(gl, GL20.GL_FRAGMENT_SHADER, fsSource)

        println("glCreateProgram")
        val shaderProgram = gl.createProgram()

        println("glAttachShader vertexShader")
        gl.attachShader(shaderProgram, vertexShader)

        println("glAttachShader fragmentShader")
        gl.attachShader(shaderProgram, fragmentShader)

        println("shaderProgram")
        gl.linkProgram(shaderProgram)

        println("return shaderProgram")
        return shaderProgram
    }

    private fun loadShader(gl: WebGLRenderingContext, type: Int, source: String): WebGLShader {
        val shader = gl.createShader(type)

        gl.shaderSource(shader, source)
        gl.compileShader(shader)

        return shader
    }

}