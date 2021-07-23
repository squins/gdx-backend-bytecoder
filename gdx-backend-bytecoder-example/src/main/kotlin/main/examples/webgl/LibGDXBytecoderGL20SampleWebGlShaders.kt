package main.examples.webgl

import com.squins.gdx.backends.bytecoder.graphics.BytecoderGL20
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.BufferUtils
import com.squins.gdx.backends.bytecoder.api.web.HTMLDivElement
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import de.mirkosertic.bytecoder.api.web.OpaqueArrays
import de.mirkosertic.bytecoder.api.web.webgl.WebMat4
import main.examples.FloatConversion.convertBytecoderFloatToFloatBuffer
import java.nio.FloatBuffer

class LibGDXBytecoderGL20SampleWebGlShaders(
        private val app: HTMLDivElement,
        private val libgdxAppCanvas: LibgdxAppCanvas,
        private val libGdxGl20: BytecoderGL20
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
                                     val attribLocations: AttribLocations,
                                     val uniformLocations: UniformLocations)

    data class AttribLocations(val vertexPosition: Int)

    data class UniformLocations(val projectionMatrix: Int,
                                val modelViewMatrix: Int)

    private fun programInfo(shaderProgram: Int): ShaderProgrammingInfo {
        println("Creating programInfo")

        println("creat attribLocations")
        val attribLocations = AttribLocations(libGdxGl20.glGetAttribLocation(shaderProgram, "aVertexPosition"))
        println("create uniformLocations")

        println("Create projectionMatrix")
        val projectionMatrix = libGdxGl20.glGetUniformLocation(shaderProgram, "uProjectionMatrix")

        println("Create modelViewMatrix")
        val modelViewMatrix = libGdxGl20.glGetUniformLocation(shaderProgram, "uModelViewMatrix")
        println("Created modelViewMatrix")
        val uniformLocations = UniformLocations(
                projectionMatrix,
                modelViewMatrix
        )
        println("created uniformLocations")
        return ShaderProgrammingInfo(
                program = shaderProgram,
                attribLocations = attribLocations,
                uniformLocations = uniformLocations
        )
    }

    private fun initBuffers(libGdxGl20: BytecoderGL20): Int {
        println("glGenBuffer")
        val positionBufferId = libGdxGl20.glGenBuffer()

        println("glBindBuffer")
        libGdxGl20.glBindBuffer(GL20.GL_ARRAY_BUFFER, positionBufferId)

        println("FloatBuffer.allocate")

        println("positions.put(floatArrayOf")
        val positionsKotlinArray = floatArrayOf(
                -1.0F, 1.0F,
                1.0F, 1.0F,
                -1.0F, -1.0F,
                1.0F, -1.0F
        )
        val positionsBuffer: FloatBuffer = FloatBuffer.allocate(positionsKotlinArray.size)

        positionsBuffer.put(positionsKotlinArray)

        val bytecoderArray = OpaqueArrays.createFloatArray(positionsKotlinArray.size)

        positionsKotlinArray.forEachIndexed{ index, value -> bytecoderArray.setFloat(index, value)}

        libGdxGl20.glBufferData(GL20.GL_ARRAY_BUFFER, 8, positionsBuffer, GL20.GL_STATIC_DRAW)

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

        println("clearColor, ClearDepthf, Enable, DepthFunc")

        // Clear the canvas before we start drawing on it.
        libGdxGl20.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        println("canvas Clear")

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

        println("mat4 stuff")

        val numComponents = 2               // pull out 2 values per iteration
        val type : Int = GL20.GL_FLOAT      // the data in the buffer is 32bit floats
        val normalize = false               // don't normalize
        val stride = 0                      // how many bytes to get from one set of values to the next
        val offset = 0                      // how many bytes inside the buffer to start from



        libGdxGl20.glBindBuffer(GL20.GL_ARRAY_BUFFER, bufferId)

        println("bindBuffer")

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

        val buffer = convertBytecoderFloatToFloatBuffer(projectionMatrix)

        for(i in 0 until buffer.limit()){
            println("for loop in projectionMatrix" + buffer.get(i))
        }

        println("before uniformMatrix4fv - projection")
        libGdxGl20.glUniformMatrix4fv(
                programmingInfo.uniformLocations.projectionMatrix,
                0,
                false,
                buffer)

        val buffer1 = convertBytecoderFloatToFloatBuffer(modelViewMatrix)

        for(i in 0 until buffer1.limit()){
            println("for loop in modelviewMatrix" + buffer.get(i))
        }
        println("before uniformMatrix4fv - modelViewMatrix")
        libGdxGl20.glUniformMatrix4fv(
                programmingInfo.uniformLocations.modelViewMatrix,
                0,
                false,
                buffer1)

        println("after uniformMatrix4fv")

        val vertexCount = 4
        libGdxGl20.glDrawArrays(GL20.GL_TRIANGLE_STRIP, offset, vertexCount)

        println("retrieved GL")
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

        println("libGdxGl20.glShaderSource(shader, source)")
        libGdxGl20.glShaderSource(shader, source)

        println("libGdxGl20.glCompileShader(shader)")
        libGdxGl20.glCompileShader(shader)


        println("val intbuf = BufferUtils.newIntBuffer(1)");
        val intbuf = BufferUtils.newIntBuffer(1)

        println("libGdxGl20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, intbuf);")
        libGdxGl20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, intbuf);

        println("val compiled = intbuf[0]")
        val compiled = intbuf[0]
        if (compiled > 0) {
            println("COMPILED!!")
        }

//        if (!libGdxGl20.glGetShaderParameterBoolean(shader, GL20.GL_COMPILE_STATUS)) {
//            println('An error occurred compiling the shaders: ' + libGdxGl20.glGetShaderInfoLog(shader));
//            libGdxGl20.glDeleteShader(shader);
//            return null
//        }

        return shader
    }

    private fun convertToBytecoder(positionsKotlinArray: FloatArray): de.mirkosertic.bytecoder.api.web.FloatArray {
        val bytecoderArray = OpaqueArrays.createFloatArray(positionsKotlinArray.size)

        positionsKotlinArray.forEachIndexed { index, value -> bytecoderArray.setFloat(index, value) }
        return bytecoderArray
    }
}