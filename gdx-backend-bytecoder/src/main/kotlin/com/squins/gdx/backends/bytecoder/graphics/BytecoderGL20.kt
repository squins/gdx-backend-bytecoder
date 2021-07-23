package com.squins.gdx.backends.bytecoder.graphics

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.squins.gdx.backends.bytecoder.makeAndLogIllegalArgumentException
import de.mirkosertic.bytecoder.api.web.Int8Array
import de.mirkosertic.bytecoder.api.web.IntArray
import de.mirkosertic.bytecoder.api.web.OpaqueArrays
import de.mirkosertic.bytecoder.api.web.webgl.*
import java.nio.*
import kotlin.experimental.and


const val tag = "BytecoderGL20"

class BytecoderGL20(private val delegate: WebGLRenderingContext) : GL20 {

    private var lastCreatedShader:Int = 0
    private var shaders: MutableMap<Int, WebGLShader> = mutableMapOf()

    private var lastCreatedProgram: Int = 0
    private var programs: MutableMap<Int, WebGLProgram> = mutableMapOf()

    private var lastCreatedBuffer: Int = 0
    private var buffers: MutableMap<Int, WebGLBuffer> = mutableMapOf()

    private var lastCreatedUniformLocation: Int = 0
    private var uniforms: MutableMap<Int, MutableMap<Int, WebGLUniformLocation>> = mutableMapOf()

    private var lastCreatedRenderBuffer: Int = 0
    private var renderBuffers: MutableMap<Int, WebGLRenderBuffer> = mutableMapOf()

    private var lastCreatedFrameBuffer: Int = 0
    private var frameBuffers: MutableMap<Int, WebGLFrameBuffer> = mutableMapOf()

    private var lastCreatedTexture: Int = -1
    private var textures: MutableMap<Int, WebGLTexture> = mutableMapOf()

    private var currProgram = 0

    private fun getUniformLocation(location: Int): WebGLUniformLocation {
        return uniforms[currProgram]?.get(location)!!
    }

    override fun glUniform3i(location: Int, x: Int, y: Int, z: Int) {
        delegate.uniform3i(getUniformLocation(location), x, y, z)
    }

    override fun glLineWidth(width: Float) {
        delegate.lineWidth(width)
    }

    override fun glDeleteShader(shaderId: Int) {
        val shader = shaders.remove(shaderId)
        if (shader == null) {
            throw makeAndLogIllegalArgumentException(tag, "Shader not found: $shader")
        }
        delegate.deleteShader(shader)
    }

    override fun glDetachShader(program: Int, shader: Int) {
        delegate.detachShader(programs.getProgram(program), shaders.getShader(shader))
    }

    override fun glVertexAttrib3f(indx: Int, x: Float, y: Float, z: Float) {
        delegate.vertexAttrib3f(indx, x, y, z)
    }

    override fun glCompileShader(shader: Int) {
        delegate.compileShader(shaders.getShader(shader))
    }

    override fun glTexParameterfv(target: Int, pname: Int, params: FloatBuffer) {
        delegate.texParameterf(target, pname, params.get())
    }

    override fun glStencilFunc(func: Int, ref: Int, mask: Int) {
        delegate.stencilFunc(func, ref, mask)
    }

    override fun glDeleteFramebuffer(framebuffer: Int) {
        val frameBuffer = frameBuffers.remove(framebuffer)
        if (frameBuffer == null) {
            throw makeAndLogIllegalArgumentException(tag, "frameBuffer not found: $frameBuffer")
        }
        delegate.deleteFramebuffer(frameBuffer)
    }

    override fun glGenTexture(): Int {
        val createTexture = delegate.createTexture()
        val textureId = ++lastCreatedTexture
        textures[textureId] = createTexture
        // DISABLED: performance println("glGenTexture $textureId")
        return textureId
    }

    override fun glBindAttribLocation(program: Int, index: Int, name: String?) {
        delegate.bindAttribLocation(programs.getProgram(program), index, name)
    }

    override fun glEnableVertexAttribArray(index: Int) {
        delegate.enableVertexAttribArray(index)
    }

    override fun glReleaseShaderCompiler() {
        throw makeAndLogIllegalArgumentException(tag, "not implemented")
    }

    override fun glUniform2f(location: Int, x: Float, y: Float) {
        delegate.uniform2f(getUniformLocation(location), x, y)
    }

    override fun glGetActiveAttrib(program: Int, index: Int, size: IntBuffer, type: IntBuffer): String {
        val activeAttrib: WebGLActiveInfo = delegate.getActiveAttrib(programs.getProgram(program), index)
        size.put(activeAttrib.getSize())
        type.put(activeAttrib.getType())
        return activeAttrib.getName()
    }

    override fun glGenFramebuffer(): Int {
        val createFrameBuffer = delegate.createFramebuffer()
        val frameBufferId = ++lastCreatedFrameBuffer
        frameBuffers[frameBufferId] = createFrameBuffer
        return frameBufferId
    }

    override fun glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer) {
        delegate.uniformMatrix2fv(getUniformLocation(location), transpose, convertBufferToFloatArray(value))
    }

    override fun glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int) {
        delegate.uniformMatrix2fv(getUniformLocation(location), transpose, convertFloatArray(value))
    }

    override fun glUniform2fv(location: Int, count: Int, v: FloatBuffer) {
        delegate.uniform2fv(getUniformLocation(location), convertBufferToFloatArray(v))
    }

    override fun glUniform2fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform2fv(getUniformLocation(location), convertFloatArray(v))
    }

    override fun glUniform4iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform4iv(getUniformLocation(location),convertIntBufferToIntArray(v))
    }

    override fun glUniform4iv(location: Int, count: Int, v: kotlin.IntArray, offset: Int) {
        delegate.uniform4iv(getUniformLocation(location), intArrayToBytecoderIntArray(v))
    }

    override fun glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) {
        delegate.colorMask(red, green, blue, alpha)
    }

    override fun glPolygonOffset(factor: Float, units: Float) {
        delegate.polygonOffset(factor, units)
    }

    override fun glViewport(x: Int, y: Int, width: Int, height: Int) {
        delegate.viewport(x, y, width, height)
    }

    override fun glGetProgramiv(program: Int, pname: Int, params: IntBuffer) {
        if (pname == GL20.GL_DELETE_STATUS || pname == GL20.GL_LINK_STATUS || pname == GL20.GL_VALIDATE_STATUS) {
            val program1 = programs.getProgram(program)
            val result: Boolean = delegate.getProgramParameterBoolean(program1, pname)
            // DISABLED: performance println("Result: $result")
            params.put(0, if (result) GL20.GL_TRUE else GL20.GL_FALSE)
            // DISABLED: performance println("params.get(0):" + params.get(0))
            // DISABLED: performance println("params hasArray: ${params.hasArray()}")
        } else {
            params.put(delegate.getProgramParameterInt(programs.getProgram(program), pname))
        }
    }

    override fun glGetBooleanv(pname: Int, params: Buffer?) {
        throw makeAndLogIllegalArgumentException(tag, "not implemented")
    }

    override fun glGetBufferParameteriv(target: Int, pname: Int, params: IntBuffer) {
        delegate.getBufferParameteriv(target, pname, convertIntBufferToIntArray(params))
    }

    override fun glDeleteTexture(texture: Int) {
        val textureToUse = textures.remove(texture)
                ?: throw makeAndLogIllegalArgumentException(tag, "Shader not found: $texture")
        delegate.deleteTexture(textureToUse)
    }

    override fun glGetVertexAttribiv(index: Int, pname: Int, params: IntBuffer) {
        throw makeAndLogIllegalArgumentException(tag, "not implemented")
    }

    override fun glVertexAttrib4fv(indx: Int, values: FloatBuffer) {
        // DISABLED: performance println("glVertexAttrib4fv")
        delegate.vertexAttrib4fv(indx, convertBufferToFloatArray(values))
    }

    override fun glTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, type: Int, pixels: Buffer?) {
        if (pixels!!.limit() > 1) {
//            val arrayHolder: HasArrayBufferView = pixels as HasArrayBufferView
//            val webGLArray: ArrayBufferView = arrayHolder.getTypedArray()
//            val buffer: ArrayBufferView
//            buffer = if (pixels is FloatBuffer) {
//                webGLArray
//            } else {
//                val remainingBytes = pixels!!.remaining() * 4
//                val byteOffset = ArrayBufferView.byteOffset() + pixels!!.position() * 4
//                Uint8ArrayNative.create(ArrayBufferView.buffer(), byteOffset, remainingBytes)
//            }
            if(pixels is ByteBuffer) {
                delegate.texSubImage2D(target, level, xoffset, yoffset, width, height, format, type, convertByteBufferToInt8Array(pixels))
            }
        } else {
            val pixmap = Pixmap.pixmaps[(pixels as IntBuffer)[0]]
            //TODO fixme
//            delegate.texSubImage2D(target, level, xoffset, yoffset, format, type, (pixmap.canvasElement))
        }
    }

    override fun glDeleteRenderbuffers(n: Int, renderbuffers: IntBuffer) {
        for (i in 0 until n) {
            val id = renderbuffers.get()
            val renderBuffer = this.renderBuffers.remove(id)
            if (renderBuffer != null) {
                delegate.deleteRenderbuffer(renderBuffer)
            }
        }
    }

    override fun glGetTexParameteriv(target: Int, pname: Int, params: IntBuffer) {
        throw makeAndLogIllegalArgumentException(tag, "glGetTexParameter not supported by GWT WebGL backend")
    }

    override fun glGenTextures(n: Int, textures: IntBuffer) {
        for (i in 0 until n) {
            val texture: WebGLTexture = delegate.createTexture()
            val textureId = ++lastCreatedTexture
            this.textures[textureId] = texture
        }
        // DISABLED: performance println("Textures size: " + this.textures.size)
    }

    override fun glStencilOpSeparate(face: Int, fail: Int, zfail: Int, zpass: Int) {
        delegate.stencilOpSeparate(face, fail, zfail, zpass)
    }

    override fun glUniform2i(location: Int, x: Int, y: Int) {
        delegate.uniform2i(getUniformLocation(location), x, y)
    }

    override fun glCheckFramebufferStatus(target: Int): Int {
        return delegate.checkFramebufferStatus(target)
    }

    override fun glDeleteTextures(n: Int, textures: IntBuffer) {
        for (i in 0 until n) {
            val id = textures.get()
            val texture = this.textures.remove(id)
            if (texture != null) {
                delegate.deleteTexture(texture)
            }
        }
    }

    override fun glBindRenderbuffer(target: Int, renderbuffer: Int) {
        delegate.bindRenderbuffer(target, renderBuffers.getRenderBuffer(renderbuffer))
    }

    override fun glTexParameteriv(target: Int, pname: Int, params: IntBuffer) {
        delegate.texParameterf(target, pname, params.get().toFloat())
    }

    override fun glVertexAttrib4f(indx: Int, x: Float, y: Float, z: Float, w: Float) {
        delegate.vertexAttrib4f(indx, x, y, z, w)
    }

    override fun glDeleteBuffers(n: Int, buffers: IntBuffer) {
        for (i in 0 until n) {
            val id = buffers.get()
            val buffer = this.buffers.remove(id)
            if (buffer != null) {
                delegate.deleteBuffer(buffer)
            }
        }
    }

    override fun glGetProgramInfoLog(program: Int): String {
        return delegate.getProgramInfoLog(programs.getProgram(program))
    }

    override fun glIsRenderbuffer(renderbuffer: Int): Boolean {
        return delegate.isRenderbuffer(renderBuffers.getRenderBuffer(renderbuffer))
    }

    override fun glFrontFace(mode: Int) {
        delegate.frontFace(mode)
    }

    override fun glUniform1iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform1iv(getUniformLocation(location), convertIntBufferToIntArray(v))
    }

    override fun glUniform1iv(location: Int, count: Int, v: kotlin.IntArray, offset: Int) {
        delegate.uniform1iv(getUniformLocation(location), intArrayToBytecoderIntArray(v))
    }

    override fun glBindTexture(target: Int, texture: Int) {
        // DISABLED: performance println("glBindTexture: $texture size list: ${this.textures.size}, ${textures.keys.joinToString()}")
        delegate.bindTexture(target, textures.getTexture(texture))
    }

    override fun glGetUniformLocation(program: Int, name: String): Int {
        val location: WebGLUniformLocation = delegate.getUniformLocation(programs[program]!!, name)
        var progUniforms: MutableMap<Int, WebGLUniformLocation>? = uniforms[program]
        if (progUniforms == null) {
            progUniforms = mutableMapOf()
            uniforms[program] = progUniforms
        }
        // FIXME check if uniform already stored.
        val id = ++lastCreatedUniformLocation
        progUniforms[id] = location
        return id
//        println("glGetUniformLocation, program: $program, name: $name")
//        val location: WebGLUniformLocation = delegate.getUniformLocation(programs[program]!!, name)
//        return allocateUniformLocationId(program, location)
    }

    override fun glPixelStorei(pname: Int, param: Int) {
        delegate.pixelStorei(pname, param)
    }

    override fun glHint(target: Int, mode: Int) {
        delegate.hint(target, mode)
    }

    override fun glFramebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: Int) {
        delegate.framebufferRenderbuffer(target, attachment, renderbuffertarget, renderBuffers.getRenderBuffer(renderbuffer))
    }

    override fun glUniform1f(location: Int, x: Float) {
        delegate.uniform1f(getUniformLocation(location), x)
    }

    override fun glDepthMask(flag: Boolean) {
        delegate.depthMask(flag)
    }

    override fun glBlendColor(red: Float, green: Float, blue: Float, alpha: Float) {
        delegate.blendColor(red, green, blue, alpha)
    }

    override fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer) {
        // DISABLED: performance println("("BYTECODERGL20: glUniformMatrix4fv")
        // DISABLED: performance println("("location : $location")
        // DISABLED: performance println("("count : $count")
        // DISABLED: performance println("("transpose : $transpose")
        val floatArray = convertBufferToFloatArray(value)
        // DISABLED: performance println("("floatArray created, before for")
        // DISABLED: performance println("("floatArrayLength: ${floatArray.floatArrayLength()}" )

        delegate.uniformMatrix4fv(getUniformLocation(location), transpose, floatArray)
    }

    override fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int) {
        delegate.uniformMatrix4fv(getUniformLocation(location), transpose, convertFloatArray(value))
    }

    override fun glBufferData(target: Int, size: Int, data: Buffer, usage: Int) {
        when (data) {
            is FloatBuffer -> {
                // DISABLED: performance println("("floatbuffer")
                delegate.bufferData(target, convertBufferToFloatArray(data), usage)
            }
            is ShortBuffer -> {
                // DISABLED: performance println("("shortbuffer")
                delegate.bufferData(target, convertShortBufferToFloatArray(data), usage)
            }
            else -> throw makeAndLogIllegalArgumentException("BytecoderGL20","Can only cope with FloatBuffer and ShortBuffer at the moment")
        }
        //TODO is it ok to ignore sizes? (check GWT)

    }

    override fun glValidateProgram(program: Int) {
        delegate.validateProgram(programs.getProgram(program))
    }

    override fun glTexParameterf(target: Int, pname: Int, param: Float) {
        delegate.texParameterf(target, pname, param)
    }

    override fun glIsFramebuffer(framebuffer: Int): Boolean {
        return delegate.isFramebuffer(frameBuffers.getFrameBuffer(framebuffer))
    }

    override fun glDeleteBuffer(bufferId: Int) {
        val buffer = buffers.remove(bufferId) ?: throw makeAndLogIllegalArgumentException(tag, "Buffer not found: $bufferId")
        delegate.deleteBuffer(buffer)
    }

    override fun glShaderSource(shaderId: Int, sourceCode: String) {
        delegate.shaderSource(shaders.getShader(shaderId), sourceCode)
    }

    override fun glVertexAttrib2fv(indx: Int, values: FloatBuffer) {
        // DISABLED: performance println("("glVertexAttrib2fv")
        delegate.vertexAttrib2fv(indx, convertBufferToFloatArray(values))
    }

    override fun glDeleteFramebuffers(n: Int, framebuffers: IntBuffer) {
        for (i in 0 until n) {
            val id = framebuffers.get()
            val frameBuffer = this.frameBuffers.remove(id)
            if (frameBuffer != null) {
                delegate.deleteFramebuffer(frameBuffer)
            }
        }
    }

    override fun glUniform4fv(location: Int, count: Int, v: FloatBuffer) {
        delegate.uniform4fv(getUniformLocation(location), convertBufferToFloatArray(v))
    }

    override fun glUniform4fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform4fv(getUniformLocation(location), convertFloatArray(v))
    }

    override fun glCompressedTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, imageSize: Int, data: Buffer?) {
        if (data !is ByteBuffer) {
            // DISABLED: performance println("("glCompressedTexSubImage2D data type!")
            return
        }

        delegate.compressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, convertByteBufferToInt8Array(data))
    }

    override fun glGenerateMipmap(target: Int) {
        delegate.generateMipmap(target)
    }

    override fun glDeleteProgram(programId: Int) {
        val program = programs[programId]
        if (program == null) {
            throw makeAndLogIllegalArgumentException(tag, "Program not found: $program")
        }
        programs.remove(programId)
//        uniforms1.remove(programId)
        uniforms.remove(programId)
        delegate.deleteProgram(program)
    }

    override fun glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int) {
        delegate.framebufferTexture2D(target, attachment, textarget, textures.getTexture(texture), level)
    }

    override fun glGenRenderbuffer(): Int {
        val createRenderBuffer = delegate.createRenderbuffer()
        val renderBufferId = ++lastCreatedRenderBuffer
        renderBuffers[renderBufferId] = createRenderBuffer
        return renderBufferId
    }

    override fun glAttachShader(program: Int, shader: Int) {
        delegate.attachShader(programs.getProgram(program), shaders.getShader(shader))
    }

    override fun glBindBuffer(target: Int, buffer: Int) {
        // DISABLED: performance println("("glBindBuffer called, target: $target, buffer: $buffer")
        val bufferToUse = buffers[buffer]
        // DISABLED: performance println("(buffers.size)
        if (bufferToUse != null) {
            delegate.bindBuffer(target, bufferToUse)
        }
    }

    override fun glShaderBinary(n: Int, shaders: IntBuffer, binaryformat: Int, binary: Buffer?, length: Int) {
        throw makeAndLogIllegalArgumentException(tag, "glShaderBinary not supported by Bytecoder WebGL backend")
    }

    override fun glDisable(cap: Int) {
        delegate.disable(cap)
    }

    override fun glGetRenderbufferParameteriv(target: Int, pname: Int, params: IntBuffer) {
        throw makeAndLogIllegalArgumentException(tag, "not implemented")
    }

    override fun glGetShaderInfoLog(shader: Int): String {
        return delegate.getShaderInfoLog(shaders.getShader(shader))
    }

    override fun glGetActiveUniform(program: Int, index: Int, size: IntBuffer, type: IntBuffer): String {
        val activeUniform: WebGLActiveInfo = delegate.getActiveUniform(programs.getProgram(program), index)
        size.put(activeUniform.getSize())
        type.put(activeUniform.getType())
        return activeUniform.getName()
    }

    override fun glClearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        delegate.clearColor(red, green, blue, alpha)
    }

    override fun glClearDepthf(depth: Float) {
        delegate.clearDepth(depth)
    }

    override fun glIsShader(shader: Int): Boolean {
        return delegate.isShader(shader)
    }

    override fun glUniform1i(location: Int, x: Int) {
        delegate.uniform1i(getUniformLocation(location), x)
    }

    override fun glBlendEquationSeparate(modeRGB: Int, modeAlpha: Int) {
        delegate.blendEquationSeparate(modeRGB, modeAlpha)
    }

    override fun glScissor(x: Int, y: Int, width: Int, height: Int) {
        delegate.scissor(x, y, width, height)
    }

    override fun glCreateProgram(): Int {
        val createProgram = delegate.createProgram()
        val programId = ++lastCreatedProgram
        programs[programId] = createProgram

        return programId
    }

    override fun glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer) {
        delegate.uniformMatrix3fv(getUniformLocation(location), transpose, convertBufferToFloatArray(value))
    }

    override fun glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int) {
        delegate.uniformMatrix3fv(getUniformLocation(location), transpose, convertFloatArray(value))
    }

    override fun glGetTexParameterfv(target: Int, pname: Int, params: FloatBuffer) {
       throw makeAndLogIllegalArgumentException(tag, "glGetTexParameter not supported by GWT WebGL backend")
    }

    override fun glVertexAttrib1f(indx: Int, x: Float) {
        delegate.vertexAttrib1f(indx, x)
    }

    override fun glUniform1fv(location: Int, count: Int, v: FloatBuffer) {
        delegate.uniform1fv(getUniformLocation(location), convertBufferToFloatArray(v))
    }

    override fun glUniform1fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform1fv(getUniformLocation(location), convertFloatArray(v))
    }

    override fun glUniform3iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform3iv(getUniformLocation(location), convertIntBufferToIntArray(v))
    }

    override fun glUniform3iv(location: Int, count: Int, v: kotlin.IntArray, offset: Int) {
        delegate.uniform3iv(getUniformLocation(location), intArrayToBytecoderIntArray(v))
    }

    override fun glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: Buffer?) {
        // DISABLED: performance println("("glTexImage2D")
        if (pixels == null) {
            // DISABLED: performance println("("pixels null")
            delegate.texImage2D(target, level, internalformat, width, height, border, format, type, null)
        } else {
//            if (pixels.limit() > 1) {
//                val arrayHolder: HasArrayBufferView = pixels as HasArrayBufferView
//                val webGLArray: ArrayBufferView = arrayHolder.getTypedArray()
//                val buffer: ArrayBufferView
//                buffer = if (pixels is FloatBuffer) {
//                    webGLArray
//                } else {
//                    val remainingBytes = pixels.remaining() * 4
//                    val byteOffset: Int = webGLArray.byteOffset() + pixels.position() * 4
//                    Uint8ArrayNative.create(webGLArray.buffer(), byteOffset, remainingBytes)
//                }
//                delegate.texImage2D(target, level, internalformat, width, height, border, format, type, buffer)
//            } else {
            // DISABLED: performance println("("before pixmap pixels")

            // DISABLED: performance println("("""Pixmaps size: ${Pixmap.pixmaps.size}""")
            // DISABLED: performance println("("""pixmap keys size: ${Pixmap.pixmaps.keys.size}""")
            // DISABLED: performance println("("Pixmap keys: ${Pixmap.pixmaps.keys.toIntArray().contentToString()}")

            val buffer = convertByteBufferToInt8Array(pixels as ByteBuffer)
            // DISABLED: performance println("("after convertByteBufferToInt8Array")
            // DISABLED: performance println("(buffer.getByte(0))
            val pixmap: Pixmap = Pixmap.pixmaps[0]
                    ?: // DISABLED: performance println("("Pixmap not found")
                    throw java.lang.IllegalStateException("Pixmap not found")

            // DISABLED: performance println("(pixmap.imageElement.getSrc())

            // Prefer to use the HTMLImageElement when possible, since reading from the CanvasElement can be lossy.
            // DISABLED: performance println("("before pixmap.canUseImageElement()")
            if (pixmap.canUseImageElement()) {
                // DISABLED: performance println("("useImageElement")
                delegate.texImage2D(target, level, internalformat, format, type, pixmap.imageElement)
            } else {
                // DISABLED: performance println("("useCanvasElement")
                // TODO: use CanvasElement, not Libgdx internal thingie
//                    delegate.texImage2D(target, level, internalformat, format, type, pixmap.getCanvasElement())
            }
//            }
        }
        // DISABLED: performance println("("after glTexImage2D")

//        delegate.texImage2D(target, level, internalformat, width, height, border, format, type, convertByteBufferToInt8Array(pixels))
    }

    override fun glVertexAttrib3fv(indx: Int, values: FloatBuffer) {
        // DISABLED: performance println("("glVertexAttrib3fv")
        delegate.vertexAttrib3fv(indx, convertBufferToFloatArray(values))
    }

    override fun glBlendFunc(sfactor: Int, dfactor: Int) {
        delegate.blendFunc(sfactor, dfactor)
    }

    override fun glIsEnabled(cap: Int): Boolean {
        return delegate.isEnabled(cap)
    }

    override fun glGetAttribLocation(program: Int, name: String): Int {
        val prog = programs.getProgram(program)
        return delegate.getAttribLocation(prog, name)
    }

    override fun glDepthRangef(zNear: Float, zFar: Float) {
        delegate.depthRangef(zNear, zFar)
    }

    override fun glFlush() {
        delegate.flush()
    }

    override fun glSampleCoverage(value: Float, invert: Boolean) {
        delegate.sampleCoverage(value, invert)
    }

    override fun glCopyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int) {
        delegate.copyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height)
    }

    /**
     * status: copied, not verified.
     */
    override fun glGetShaderiv(shaderId: Int, pname: Int, params: IntBuffer) {
        if (pname == GL20.GL_COMPILE_STATUS || pname == GL20.GL_DELETE_STATUS) {
            val shader = shaders.getShader(shaderId)

            val result: Boolean = delegate.getShaderParameterBoolean(shader, pname)
            // DISABLED: performance println("("glGetShaderiv() - Result: $result")
            val valueToPut = if (result) GL20.GL_TRUE else GL20.GL_FALSE
            // DISABLED: performance println("("glGetShaderiv valueToPut: $valueToPut")
            params.put(valueToPut)
            // DISABLED: performance println("("params.get(0):" + params.get(0))
            // DISABLED: performance println("("params hasArray: ${params.hasArray()}")
        } else {
            val result: Int = delegate.getShaderParameteri(shaders.getShader(shaderId), pname)
            params.put(result)
        }
    }

    override fun glGetUniformfv(program: Int, location: Int, params: FloatBuffer) {
       throw makeAndLogIllegalArgumentException(tag, "not implemented")
    }

    override fun glUniform4f(location: Int, x: Float, y: Float, z: Float, w: Float) {
        delegate.uniform4f(getUniformLocation(location), x, y, z, w)
    }

    override fun glClear(mask: Int) {
        delegate.clear(mask)
    }

    override fun glDepthFunc(func: Int) {
        delegate.depthFunc(func)
    }

    override fun glIsBuffer(buffer: Int): Boolean {
        return delegate.isBuffer(buffers.getBuffer(buffer))
    }

    override fun glVertexAttribPointer(indx: Int, size: Int, type: Int, normalized: Boolean, stride: Int, ptr: Buffer?) {
        throw makeAndLogIllegalArgumentException(tag, "not implemented, vertex arrays aren't support in WebGL it seems")
    }

    override fun glVertexAttribPointer(indx: Int, size: Int, type: Int, normalized: Boolean, stride: Int, ptr: Int) {
        delegate.vertexAttribPointer(indx, size, type, normalized, stride, ptr)
    }

    override fun glStencilMaskSeparate(face: Int, mask: Int) {
        delegate.stencilMaskSeparate(face, mask)
    }

    override fun glDrawElements(mode: Int, count: Int, type: Int, indices: Buffer?) {
        if (indices != null) {
            delegate.drawElements(mode, count, type, indices.position()) //FIXME check GWT
        }
    }

    override fun glDrawElements(mode: Int, count: Int, type: Int, indices: Int) {
        delegate.drawElements(mode, count, type, indices)
    }

    override fun glTexParameteri(target: Int, pname: Int, param: Int) {
        delegate.texParameterf(target, pname, param.toFloat())
    }

    override fun glUseProgram(program: Int) {
        currProgram = program
        delegate.useProgram(programs.getProgram(program))
    }

    override fun glFinish() {
        delegate.finish()
    }

    override fun glGetIntegerv(pname: Int, params: IntBuffer) {
        if (pname == GL20.GL_ACTIVE_TEXTURE || pname == GL20.GL_ALPHA_BITS || pname == GL20.GL_BLEND_DST_ALPHA
                || pname == GL20.GL_BLEND_DST_RGB || pname == GL20.GL_BLEND_EQUATION_ALPHA || pname == GL20.GL_BLEND_EQUATION_RGB
                || pname == GL20.GL_BLEND_SRC_ALPHA || pname == GL20.GL_BLEND_SRC_RGB || pname == GL20.GL_BLUE_BITS
                || pname == GL20.GL_CULL_FACE_MODE || pname == GL20.GL_DEPTH_BITS || pname == GL20.GL_DEPTH_FUNC
                || pname == GL20.GL_FRONT_FACE || pname == GL20.GL_GENERATE_MIPMAP_HINT || pname == GL20.GL_GREEN_BITS
                || pname == GL20.GL_IMPLEMENTATION_COLOR_READ_FORMAT || pname == GL20.GL_IMPLEMENTATION_COLOR_READ_TYPE
                || pname == GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS || pname == GL20.GL_MAX_CUBE_MAP_TEXTURE_SIZE
                || pname == GL20.GL_MAX_FRAGMENT_UNIFORM_VECTORS || pname == GL20.GL_MAX_RENDERBUFFER_SIZE
                || pname == GL20.GL_MAX_TEXTURE_IMAGE_UNITS || pname == GL20.GL_MAX_TEXTURE_SIZE || pname == GL20.GL_MAX_VARYING_VECTORS
                || pname == GL20.GL_MAX_VERTEX_ATTRIBS || pname == GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS
                || pname == GL20.GL_MAX_VERTEX_UNIFORM_VECTORS || pname == GL20.GL_NUM_COMPRESSED_TEXTURE_FORMATS
                || pname == GL20.GL_PACK_ALIGNMENT || pname == GL20.GL_RED_BITS || pname == GL20.GL_SAMPLE_BUFFERS
                || pname == GL20.GL_SAMPLES || pname == GL20.GL_STENCIL_BACK_FAIL || pname == GL20.GL_STENCIL_BACK_FUNC
                || pname == GL20.GL_STENCIL_BACK_PASS_DEPTH_FAIL || pname == GL20.GL_STENCIL_BACK_PASS_DEPTH_PASS
                || pname == GL20.GL_STENCIL_BACK_REF || pname == GL20.GL_STENCIL_BACK_VALUE_MASK
                || pname == GL20.GL_STENCIL_BACK_WRITEMASK || pname == GL20.GL_STENCIL_BITS || pname == GL20.GL_STENCIL_CLEAR_VALUE
                || pname == GL20.GL_STENCIL_FAIL || pname == GL20.GL_STENCIL_FUNC || pname == GL20.GL_STENCIL_PASS_DEPTH_FAIL
                || pname == GL20.GL_STENCIL_PASS_DEPTH_PASS || pname == GL20.GL_STENCIL_REF || pname == GL20.GL_STENCIL_VALUE_MASK
                || pname == GL20.GL_STENCIL_WRITEMASK || pname == GL20.GL_SUBPIXEL_BITS || pname == GL20.GL_UNPACK_ALIGNMENT) {
            params.put(0, delegate.getParameteri(pname))
        }
//        else if (pname == GL20.GL_VIEWPORT) {
//            val array : IntArray = delegate.getParameterv(pname);
//            params.put(0, array.getIntValue(0));
//            params.put(1, array.getIntValue(1));
//            params.put(2, array.getIntValue(2));
//            params.put(3, array.getIntValue(3));
//            params.flip();
//        } else if(pname == GL20.GL_FRAMEBUFFER_BINDING) {
//            val fbo : WebGLFrameBuffer = delegate.getParametero(pname);
//            if(fbo == null) {
//                params.put(0);
//            } else {
//                params.put(frameBuffers.get));
//            }
//
//           params.flip();
          else
            throw makeAndLogIllegalArgumentException("BytecoderGL20","glGetInteger not supported by Bytecoder WebGL backend")
    }

    override fun glBlendEquation(mode: Int) {
        delegate.blendEquation(mode)
    }

    override fun glUniform4i(location: Int, x: Int, y: Int, z: Int, w: Int) {
        delegate.uniform4i(getUniformLocation(location), x, y, z, w)
    }

    override fun glVertexAttrib1fv(indx: Int, values: FloatBuffer) {
        // DISABLED: performance println("("glVertexAttrib1fv")
        delegate.vertexAttrib1fv(indx, convertBufferToFloatArray(values))
    }

    override fun glUniform3fv(location: Int, count: Int, v: FloatBuffer) {
        delegate.uniform3fv(getUniformLocation(location), convertBufferToFloatArray(v))
    }

    override fun glUniform3fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform3fv(getUniformLocation(location), convertFloatArray(v))
    }

    override fun glVertexAttrib2f(indx: Int, x: Float, y: Float) {
        delegate.vertexAttrib2f(indx, x, y)
    }

    override fun glActiveTexture(texture: Int) {
        delegate.activeTexture(texture)
    }

    override fun glCullFace(mode: Int) {
        delegate.cullFace(mode)
    }

    override fun glClearStencil(s: Int) {
        delegate.clearStencil(s)
    }

    override fun glGetFloatv(pname: Int, params: FloatBuffer) {
        // DISABLED: performance println("("glGetFloatv: $pname")
        if (pname == GL20.GL_DEPTH_CLEAR_VALUE || pname == GL20.GL_LINE_WIDTH || pname == GL20.GL_POLYGON_OFFSET_FACTOR
                || pname == GL20.GL_POLYGON_OFFSET_UNITS || pname == GL20.GL_SAMPLE_COVERAGE_VALUE)
            params.put(0, delegate.getParameterf(pname))
        else
            throw makeAndLogIllegalArgumentException("BytecoderGL20", "glGetFloat not supported by GWT WebGL backend")
    }

    override fun glDrawArrays(mode: Int, first: Int, count: Int) {
        delegate.drawArrays(mode, first, count)
    }

    override fun glBindFramebuffer(target: Int, framebuffer: Int) {
        delegate.bindFramebuffer(target, frameBuffers.getFrameBuffer(framebuffer))
    }

    override fun glGetError(): Int {
        return delegate.getError()
    }

    override fun glBufferSubData(target: Int, offset: Int, size: Int, data: Buffer?) {
        // DISABLED: performance println("("glBufferSubData called")
        when (data) {
            is FloatBuffer -> {
                delegate.bufferSubData(target, offset, convertBufferToFloatArray(data))
            }
            is ShortBuffer -> {
                delegate.bufferSubData(target, offset, convertShortBufferToFloatArray(data))
            }
            else -> throw makeAndLogIllegalArgumentException("BytecoderGL20", "Can only copy with FloatBuffer and ShortBuffer a the moment")
        }
    }

    override fun glCopyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int) {
        delegate.copyTexImage2D(target, level, internalformat, x, y, width, height, border)
    }

    override fun glIsProgram(program: Int): Boolean {
        return delegate.isProgram(programs.getProgram(program))
    }

    override fun glStencilOp(fail: Int, zfail: Int, zpass: Int) {
        delegate.stencilOp(fail, zfail, zpass)
    }

    override fun glDisableVertexAttribArray(index: Int) {
        delegate.disableVertexAttribArray(index)
    }

    override fun glGenBuffers(n: Int, buffers: IntBuffer) {
        // DISABLED: performance println("("glGenBuffers called!!!!!!")
        for (i in 0 until n) {
            val buffer: WebGLBuffer = delegate.createBuffer()
            val bufferId = ++lastCreatedBuffer
            this.buffers[bufferId] = buffer
            buffers.put(bufferId)
        }
    }

    override fun glGetAttachedShaders(program: Int, maxcount: Int, count: Buffer?, shaders: IntBuffer) {
        throw makeAndLogIllegalArgumentException(tag, "not implemented")
    }

    override fun glGenRenderbuffers(n: Int, renderbuffers: IntBuffer) {
        for (i in 0 until n) {
            val renderBuffer = delegate.createRenderbuffer()
            val id = ++lastCreatedRenderBuffer
            this.renderBuffers[id] = renderBuffer
            renderbuffers.put(id)
        }
    }

    override fun glRenderbufferStorage(target: Int, internalformat: Int, width: Int, height: Int) {
        delegate.renderbufferStorage(target, internalformat, width, height)
    }

    override fun glUniform3f(location: Int, x: Float, y: Float, z: Float) {
        delegate.uniform3f(getUniformLocation(location), x, y, z)
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    override fun glReadPixels(x: Int, y: Int, width: Int, height: Int, format: Int, type: Int, pixels: Buffer?) {
        if (format !== GL20.GL_RGBA || type !== GL20.GL_UNSIGNED_BYTE) {
            throw makeAndLogIllegalArgumentException(
                tag,
                    "Only format RGBA and type UNSIGNED_BYTE are currently supported for glReadPixels(...). Create an issue when you need other formats.")
        }
        if (pixels !is ByteBuffer) {
            throw makeAndLogIllegalArgumentException(tag, "Inputed pixels buffer needs to be of type ByteBuffer for glReadPixels(...).")
        }

        // create new ArrayBufferView (4 bytes per pixel)

        // create new ArrayBufferView (4 bytes per pixel)
        val size = 4 * width * height
        val buffer: Int8Array = OpaqueArrays.createInt8Array(size)

        // read bytes to ArrayBufferView

        // read bytes to ArrayBufferView
        delegate.readPixels(x, y, width, height, format, type, buffer)

        // copy ArrayBufferView to our pixels array
        val pixelsByte: ByteBuffer = pixels.asReadOnlyBuffer()
        for (i in 0 until size) {
            pixelsByte.put(buffer.getByte(i))
        }
    }

    override fun glStencilMask(mask: Int) {
        delegate.stencilMask(mask)
    }

    override fun glBlendFuncSeparate(srcRGB: Int, dstRGB: Int, srcAlpha: Int, dstAlpha: Int) {
        delegate.blendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha)
    }

    override fun glGetShaderPrecisionFormat(shadertype: Int, precisiontype: Int, range: IntBuffer, precision: IntBuffer) {
        throw makeAndLogIllegalArgumentException(tag, "glGetShaderPrecisionFormat not supported by GWT WebGL backend")
    }

    override fun glIsTexture(texture: Int): Boolean {
        return delegate.isTexture(textures.getTexture(texture))
    }

    override fun glLinkProgram(program: Int) {
        delegate.linkProgram(programs.getProgram(program))
    }

    override fun glGetVertexAttribfv(index: Int, pname: Int, params: FloatBuffer) {
        throw makeAndLogIllegalArgumentException(tag, "not implemented")
    }

    override fun glGetVertexAttribPointerv(index: Int, pname: Int, pointer: Buffer?) {
        throw makeAndLogIllegalArgumentException(tag, "glGetVertexAttribPointer not supported by GWT WebGL backend")
    }

    override fun glCreateShader(type: Int): Int {
        // DISABLED: performance println("("glCreateShader called")
        val createShader = delegate.createShader(type)
        val shaderId = ++lastCreatedShader
        shaders[shaderId] = createShader
        return shaderId
    }

    override fun glStencilFuncSeparate(face: Int, func: Int, ref: Int, mask: Int) {
        delegate.stencilFuncSeparate(face, func, ref, mask)
    }

    override fun glGetString(name: Int): String {
        return delegate.getParameterString(name)
    }

    override fun glCompressedTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: Buffer?) {
       throw makeAndLogIllegalArgumentException("BytecoderGl20", "compressed textures not supported by Bytecoder WebGL backend")
    }

    private fun intArrayToBytecoderIntArray(v: kotlin.IntArray): IntArray {
        val intArray: IntArray = OpaqueArrays.createIntArray(v.size)
        for (i in v.indices) {
            intArray.setIntValue(i, v[i])
        }
        return intArray
    }

    private fun convertFloatArray(floatArray: FloatArray): de.mirkosertic.bytecoder.api.web.FloatArray {
        val floatArr = OpaqueArrays.createFloatArray(floatArray.size)

        for((index, value) in floatArray.withIndex()){
            floatArr.setFloat(index, value)
        }
        return floatArr
    }

    private fun convertIntArray(intArray: kotlin.IntArray): IntArray {
        val intArr = OpaqueArrays.createIntArray(intArray.size)

        for((index, value) in intArray.withIndex()){
            intArr.setIntValue(index, value)
        }
        return intArr
    }


    private fun convertByteBufferToInt8Array(data: ByteBuffer): Int8Array {
        val array = data.array()

        val dataInt8Array = OpaqueArrays.createInt8Array(array.size)

        for ((index, value) in array.withIndex()) {
            dataInt8Array.setByte(index, value)
        }
        return dataInt8Array
    }

    private fun convertIntBufferToIntArray(data: IntBuffer): IntArray{
        val array = data.array()

        val dataIntArray = OpaqueArrays.createIntArray(array.size)

        for((index, value) in array.withIndex()) {
            dataIntArray.setIntValue(index, value)
        }
        return dataIntArray
    }

    private fun convertBufferToFloatArray(data: Buffer): de.mirkosertic.bytecoder.api.web.FloatArray {
        if (data is FloatBuffer) {
            return convertBufferToFloatArray(data)
        } else {
            throw makeAndLogIllegalArgumentException(tag, "Sorry data type is not supported")
        }
    }

    private fun convertBufferToFloatArray(inputBuffer: FloatBuffer): de.mirkosertic.bytecoder.api.web.FloatArray {
        // DISABLED: performance println("("convertBufferToFloatArray called")
//        val buffer = inputBuffer.duplicate()
//
//        // TODO #2 rewind should not be there, but the source buffer is already navigated to the end.
//        buffer.rewind()
//
//        val result = OpaqueArrays.createFloatArray(buffer.remaining())
//        val tmp: FloatArray
//        if (buffer.hasArray()) {
//            tmp = buffer.array()
//        } else {
//            tmp = FloatArray(buffer.remaining())
//            buffer[tmp]
//        }
//        for (i in tmp.indices) {
//            // DISABLED: performance println("(" $i -  - ${tmp[i]}")
//            result.setFloat(i, tmp[i])
//        }

        val result = OpaqueArrays.createFloatArray(inputBuffer.remaining())

        var i: Int = inputBuffer.position()
        var j = 0
        while (i < inputBuffer.limit()) {
            // DISABLED: performance println("("first statement")
            // DISABLED: performance println("(" index: $i - element: ${inputBuffer.get(i)}")
            result.setFloat(j, inputBuffer.get(i))
            i++
            j++
        }

        // DISABLED: performance println("("Return convertBufferToFloatArray, result length: ${result.floatArrayLength()}")
        return result
    }

    private fun convertShortBufferToFloatArray(inputBuffer: ShortBuffer): de.mirkosertic.bytecoder.api.web.Int16Array {
        // DISABLED: performance println("("convertShortBufferToFloatArray called")

        val result = OpaqueArrays.createInt16Array(inputBuffer.remaining())

        var i: Int = inputBuffer.position()
        var j = 0
        while (i < inputBuffer.limit()) {
            result.setShort(j, inputBuffer.get(i) and 0xFFFF.toShort())
            i++
            j++
        }
        // DISABLED: performance println("("Return convertBufferToFloatArray, result length: ${result.shortArrayLength()}")
        return result
    }



    override fun glUniform2iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform2iv(getUniformLocation(location), convertIntBufferToIntArray(v))
    }

    override fun glUniform2iv(location: Int, count: Int, v: kotlin.IntArray, offset: Int) {
        val intArray: IntArray = intArrayToBytecoderIntArray(v)
        delegate.uniform2iv(getUniformLocation(location), intArray)
    }

    override fun glGenBuffer(): Int {
        // DISABLED: performance println("("glGenBuffer called")
        val createBuffer = delegate.createBuffer()
        val bufferId = ++lastCreatedBuffer
        buffers[bufferId] = createBuffer
        // DISABLED: performance println("("bufferId: $bufferId")
        return bufferId
    }

    override fun glEnable(cap: Int) {
        delegate.enable(cap)
    }

    override fun glGetUniformiv(program: Int, location: Int, params: IntBuffer) {
        throw makeAndLogIllegalArgumentException(tag, "not implemented")
    }

    override fun glGetFramebufferAttachmentParameteriv(target: Int, attachment: Int, pname: Int, params: IntBuffer) {
        throw makeAndLogIllegalArgumentException(tag, "not implemented")
    }

    override fun glDeleteRenderbuffer(renderbuffer: Int) {
        val renderBuffer = this.renderBuffers.remove(renderbuffer)
        if (renderBuffer == null) {
            throw makeAndLogIllegalArgumentException(tag, "RenderBuffer not found: $renderBuffer")
        }
        delegate.deleteRenderbuffer(renderBuffer)
    }

    override fun glGenFramebuffers(n: Int, framebuffers: IntBuffer) {
        for (i in 0 until n) {
            val fb: WebGLFrameBuffer = delegate.createFramebuffer()
            val id = ++lastCreatedFrameBuffer
            this.frameBuffers[id] = fb
            framebuffers.put(id)
        }
    }

}

fun MutableMap<Int, WebGLShader>.getShader(shaderId: Int) : WebGLShader =
        get(shaderId)?: throw IllegalStateException("Shader not found: $shaderId")

fun MutableMap<Int, WebGLProgram>.getProgram(programId: Int): WebGLProgram =
        get(programId)?: throw IllegalStateException("Program not found: $programId")

fun MutableMap<Int, WebGLBuffer>.getBuffer(bufferId: Int): WebGLBuffer =
        get(bufferId)?: throw IllegalStateException("Buffer not found: $bufferId")

fun MutableMap<Int, WebGLUniformLocation>.getUniformLocation(uniformLocationId: Int): WebGLUniformLocation =
        get(uniformLocationId) ?: throw makeAndLogIllegalArgumentException(
            tag, "getUniformLocation($uniformLocationId) " +
                "failed: no location for uniformLocationId: $uniformLocationId")

fun MutableMap<Int, WebGLRenderBuffer>.getRenderBuffer(renderBufferId: Int): WebGLRenderBuffer =
        get(renderBufferId) ?: throw makeAndLogIllegalArgumentException(
            tag, "getRenderBuffer($renderBufferId) " +
                "failed: no renderBuffer for renderBufferId: $renderBufferId")

fun MutableMap<Int, WebGLFrameBuffer>.getFrameBuffer(frameBufferId: Int): WebGLFrameBuffer =
        get(frameBufferId) ?: throw makeAndLogIllegalArgumentException(
            tag, "getFrameBuffer($frameBufferId) " +
                "failed: no frameBuffer for frameBufferId: $frameBufferId")

fun MutableMap<Int, WebGLTexture>.getTexture(textureId: Int): WebGLTexture =
        get(textureId) ?: throw makeAndLogIllegalArgumentException(
            tag, "getTexture($textureId) " +
                "failed: no texture for textureId: $textureId")