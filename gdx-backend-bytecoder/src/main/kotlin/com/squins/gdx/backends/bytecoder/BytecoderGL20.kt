package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.graphics.Pixmap
import com.squins.gdx.backends.bytecoder.api.web.webgl.*
import de.mirkosertic.bytecoder.api.web.Int8Array
import de.mirkosertic.bytecoder.api.web.IntArray
import de.mirkosertic.bytecoder.api.web.OpaqueArrays
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer


val tag = "BytecoderGL20"

class BytecoderGL20(private val delegate: WebGLRenderingContext) : GL20 {

    val someDep = SomeDep()

    private var lastCreatedShader:Int = 0
    private var shaders: MutableMap<Int, WebGLShader> = mutableMapOf()

    private var lastCreatedProgram: Int = 0
    private var programs: MutableMap<Int, WebGLProgram> = mutableMapOf()

    private var lastCreatedBuffer: Int = 0
    private var buffers: MutableMap<Int, WebGLBuffer> = mutableMapOf()

    private var lastCreatedUniformLocation: Int = 0
    private var uniforms: MutableMap<Int, WebGLUniformLocation> = mutableMapOf()

    private var lastCreatedRenderBuffer: Int = 0
    private var renderBuffers: MutableMap<Int, WebGLRenderBuffer> = mutableMapOf()

    private var lastCreatedFrameBuffer: Int = 0
    private var frameBuffers: MutableMap<Int, WebGLFrameBuffer> = mutableMapOf()

    private var lastCreatedTexture: Int = 0
    private var textures: MutableMap<Int, WebGLTexture> = mutableMapOf()

    override fun glUniform3i(location: Int, x: Int, y: Int, z: Int) {
        delegate.uniform3i(uniforms.getUniformLocation(location), x, y, z)
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
        delegate.texParameterfv(target, pname, convertBufferToFloatArray(params))
    }

    override fun glStencilFunc(func: Int, ref: Int, mask: Int) {
        delegate.stencilFunc(func, ref, mask)
    }

    override fun glDeleteFramebuffer(framebuffer: Int) {
        val frameBuffer = frameBuffers.remove(framebuffer)
        if (frameBuffer == null) {
            throw makeAndLogIllegalArgumentException(tag, "frameBuffer not found: $frameBuffer")
        }
        delegate.deleteFramebuffer(framebuffer)
    }

    override fun glGenTexture(): Int {
        val createTexture = delegate.createTexture()
        val textureId = ++lastCreatedTexture
        textures[textureId] = createTexture
        return textureId
    }

    override fun glBindAttribLocation(program: Int, index: Int, name: String?) {
        delegate.bindAttribLocation(programs.getProgram(program), index, name)
    }

    override fun glEnableVertexAttribArray(index: Int) {
        delegate.enableVertexAttribArray(index)
    }

    override fun glReleaseShaderCompiler() {
        delegate.releaseShaderCompiler()
    }

    override fun glUniform2f(location: Int, x: Float, y: Float) {
        delegate.uniform2f(uniforms.getUniformLocation(location), x, y)
    }

    override fun glGetActiveAttrib(program: Int, index: Int, size: IntBuffer, type: IntBuffer): String {
        val activeAttrib: WebGLActiveInfo = delegate.getActiveAttrib(programs.getProgram(program), index)
        size.put(activeAttrib.getSize())
        type.put(activeAttrib.getType())
        return activeAttrib.getName()
    }

    override fun glGenFramebuffer(): Int {
        val createFrameBuffer = delegate.genFramebuffer()
        val frameBufferId = ++lastCreatedFrameBuffer
        frameBuffers[frameBufferId] = createFrameBuffer
        return frameBufferId
    }

    override fun glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer) {
        delegate.uniformMatrix2fv(uniforms.getUniformLocation(location), count, transpose, convertBufferToFloatArray(value))
    }

    override fun glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int) {
        delegate.uniformMatrix2fv(uniforms.getUniformLocation(location), count, transpose, convertFloatArray(value), offset)
    }

    override fun glUniform2fv(location: Int, count: Int, v: FloatBuffer) {
        delegate.uniform2fv(uniforms.getUniformLocation(location), convertBufferToFloatArray(v))
    }

    override fun glUniform2fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform2fv(uniforms.getUniformLocation(location), convertFloatArray(v))
    }

    override fun glUniform4iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform4iv(uniforms.getUniformLocation(location),convertIntBufferToIntArray(v))
    }

    override fun glUniform4iv(location: Int, count: Int, v: kotlin.IntArray, offset: Int) {
        delegate.uniform4iv(uniforms.getUniformLocation(location), intArrayToBytecoderIntArray(v))
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
            println("Result: $result")
            params.put(0, if (result) GL20.GL_TRUE else GL20.GL_FALSE)
            println("params.get(0):" + params.get(0))
            println("params hasArray: ${params.hasArray()}")
        } else {
            params.put(delegate.getProgramParameterInt(programs.getProgram(program), pname))
        }
    }

    override fun glGetBooleanv(pname: Int, params: Buffer?) {
        if (params !is ByteBuffer) {
            println("glGetBooleanv data type!")
            return
        }

        delegate.getBooleanv(pname, convertByteBufferToInt8Array(params))
    }

    override fun glGetBufferParameteriv(target: Int, pname: Int, params: IntBuffer) {
        delegate.getBufferParameteriv(target, pname, convertIntBufferToIntArray(params))
    }

    override fun glDeleteTexture(texture: Int) {
        delegate.deleteTexture(texture)
    }

    override fun glGetVertexAttribiv(index: Int, pname: Int, params: IntBuffer) {
        delegate.getVertexAttribiv(index, pname, convertIntBufferToIntArray(params))
    }

    override fun glVertexAttrib4fv(indx: Int, values: FloatBuffer) {
        delegate.vertexAttrib4fv(indx, convertBufferToFloatArray(values))
    }

    override fun glTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, type: Int, pixels: Buffer?) {
        if (pixels !is ByteBuffer) {
            println("glTexSubImage2D data type!")
            return
        }

        delegate.texSubImage2D(target, level, xoffset, yoffset, width, height, format, type, convertByteBufferToInt8Array(pixels))
    }

    override fun glDeleteRenderbuffers(n: Int, renderbuffers: IntBuffer) {
        delegate.deleteRenderbuffers(n, convertIntBufferToIntArray(renderbuffers))
    }

    override fun glGetTexParameteriv(target: Int, pname: Int, params: IntBuffer) {
        delegate.getTexParameteriv(target, pname, convertIntBufferToIntArray(params))
    }

    override fun glGenTextures(n: Int, textures: IntBuffer) {
        delegate.genTextures(n, convertIntBufferToIntArray(textures))
    }

    override fun glStencilOpSeparate(face: Int, fail: Int, zfail: Int, zpass: Int) {
        delegate.stencilOpSeparate(face, fail, zfail, zpass)
    }

    override fun glUniform2i(location: Int, x: Int, y: Int) {
        delegate.uniform2i(uniforms.getUniformLocation(location), x, y)
    }

    override fun glCheckFramebufferStatus(target: Int): Int {
        return delegate.checkFramebufferStatus(target)
    }

    override fun glDeleteTextures(n: Int, textures: IntBuffer) {
        delegate.deleteTextures(n, convertIntBufferToIntArray(textures))
    }

    override fun glBindRenderbuffer(target: Int, renderbuffer: Int) {
        delegate.bindRenderbuffer(target, renderBuffers.getRenderBuffer(renderbuffer))
    }

    override fun glTexParameteriv(target: Int, pname: Int, params: IntBuffer) {
        delegate.texParameteriv(target, pname, convertIntBufferToIntArray(params))
    }

    override fun glVertexAttrib4f(indx: Int, x: Float, y: Float, z: Float, w: Float) {
        delegate.vertexAttrib4f(indx, x, y, z, w)
    }

    override fun glDeleteBuffers(n: Int, buffers: IntBuffer) {
        delegate.deleteBuffers(n, convertIntBufferToIntArray(buffers))
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
        delegate.uniform1iv(uniforms.getUniformLocation(location), count, convertIntBufferToIntArray(v))
    }

    override fun glUniform1iv(location: Int, count: Int, v: kotlin.IntArray, offset: Int) {
        delegate.uniform1iv(uniforms.getUniformLocation(location), count, v, offset)
    }

    override fun glBindTexture(target: Int, texture: Int) {
        delegate.bindTexture(target, textures.getTexture(texture))
    }

    override fun glGetUniformLocation(program: Int, name: String): Int {
        val getUniformLocation = delegate.getUniformLocation(programs.getProgram(program), name)
        val createdId = ++lastCreatedUniformLocation
        uniforms[createdId] = getUniformLocation
        return createdId
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
        delegate.uniform1f(uniforms.getUniformLocation(location), x)
    }

    override fun glDepthMask(flag: Boolean) {
        delegate.depthMask(flag)
    }

    override fun glBlendColor(red: Float, green: Float, blue: Float, alpha: Float) {
        delegate.blendColor(red, green, blue, alpha)
    }

    override fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer) {
        println("BYTECODERGL20: glUniformMatrix4fv")
        println("location : $location")
        println("count : $count")
        println("transpose : $transpose")
        val floatArray = convertBufferToFloatArray(value)
        println("floatArray created, before for")
        println("floatArrayLength: ${floatArray.floatArrayLength()}" )
        for(i in 0 until floatArray.floatArrayLength()) {
            println("BYTECODERGL20: get at index: $i source:  ${floatArray.getFloat(i)}")
        }

        println("Calling delegate.uniformMatrix4fv")
        delegate.uniformMatrix4fv(uniforms.getUniformLocation(location), transpose, floatArray)
    }

    override fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int) {
        delegate.uniformMatrix4fv(uniforms.getUniformLocation(location), transpose, convertFloatArray(value))
    }

    override fun glBufferData(target: Int, size: Int, data: Buffer, usage: Int) {
        //TODO is it ok to ignore sizes? (check GWT)
        delegate.bufferData(target, convertBufferToFloatArray(data), usage)
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

    override fun glDeleteBuffer(buffer: Int) {
        delegate.deleteBuffer(buffers.getBuffer(buffer))
    }

    override fun glShaderSource(shaderId: Int, sourceCode: String) {
        delegate.shaderSource(shaders.getShader(shaderId), sourceCode)
    }

    override fun glVertexAttrib2fv(indx: Int, values: FloatBuffer) {
        delegate.vertexAttrib2fv(indx, convertBufferToFloatArray(values))
    }

    override fun glDeleteFramebuffers(n: Int, framebuffers: IntBuffer) {
        delegate.deleteFramebuffers(n, convertIntBufferToIntArray(framebuffers))
    }

    override fun glUniform4fv(location: Int, count: Int, v: FloatBuffer) {
        delegate.uniform4fv(uniforms.getUniformLocation(location), convertBufferToFloatArray(v))
    }

    override fun glUniform4fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform4fv(uniforms.getUniformLocation(location), convertFloatArray(v))
    }

    override fun glCompressedTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, imageSize: Int, data: Buffer?) {
        if (data !is ByteBuffer) {
            println("glCompressedTexSubImage2D data type!")
            return
        }

        delegate.compressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, convertByteBufferToInt8Array(data))
    }

    override fun glGenerateMipmap(target: Int) {
        delegate.generateMipmap(target)
    }

    override fun glDeleteProgram(programId: Int) {
        val program = programs.remove(programId)
        if (program == null) {
            throw makeAndLogIllegalArgumentException(tag, "Program not found: $program")
        }
        delegate.deleteProgram(program)
    }

    override fun glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int) {
        delegate.framebufferTexture2D(target, attachment, textarget, textures.getTexture(texture), level)
    }

    override fun glGenRenderbuffer(): Int {
        val createRenderBuffer = delegate.genRenderbuffer()
        val renderBufferId = ++lastCreatedRenderBuffer
        renderBuffers[renderBufferId] = createRenderBuffer
        return renderBufferId
    }

    override fun glAttachShader(program: Int, shader: Int) {
        delegate.attachShader(programs.getProgram(program), shaders.getShader(shader))
    }

    override fun glBindBuffer(target: Int, buffer: Int) {
        delegate.bindBuffer(target, buffers.getBuffer(buffer))
    }

    override fun glShaderBinary(n: Int, shaders: IntBuffer, binaryformat: Int, binary: Buffer?, length: Int) {
        if (binary !is ByteBuffer) {
            println("glShaderBinary data type!")
            return
        }

        delegate.shaderBinary(n, convertIntBufferToIntArray(shaders), binaryformat, convertByteBufferToInt8Array(binary), length)
    }

    override fun glDisable(cap: Int) {
        delegate.disable(cap)
    }

    override fun glGetRenderbufferParameteriv(target: Int, pname: Int, params: IntBuffer) {
        delegate.getRenderbufferParameteriv(target, pname, convertIntBufferToIntArray(params))
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
        delegate.uniform1i(uniforms.getUniformLocation(location), x)
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
        delegate.uniformMatrix3fv(uniforms.getUniformLocation(location), transpose, convertBufferToFloatArray(value))
    }

    override fun glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int) {
        delegate.uniformMatrix3fv(uniforms.getUniformLocation(location), transpose, convertFloatArray(value))
    }

    override fun glGetTexParameterfv(target: Int, pname: Int, params: FloatBuffer) {
        delegate.getTexParameterfv(target, pname, convertBufferToFloatArray(params))
    }

    override fun glVertexAttrib1f(indx: Int, x: Float) {
        delegate.vertexAttrib1f(indx, x)
    }

    override fun glUniform1fv(location: Int, count: Int, v: FloatBuffer) {
        delegate.uniform1fv(uniforms.getUniformLocation(location), convertBufferToFloatArray(v))
    }

    override fun glUniform1fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform1fv(uniforms.getUniformLocation(location), convertFloatArray(v))
    }

    override fun glUniform3iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform3iv(uniforms.getUniformLocation(location), convertIntBufferToIntArray(v))
    }

    override fun glUniform3iv(location: Int, count: Int, v: kotlin.IntArray, offset: Int) {
        delegate.uniform3iv(uniforms.getUniformLocation(location), intArrayToBytecoderIntArray(v))
    }

    override fun glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: Buffer?) {
        println("glTexImage2D")
        if (pixels == null) {
            println("pixels null")
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
            println("before pixmap pixels")
            val buffer = convertByteBufferToInt8Array(pixels as ByteBuffer)
            println("after convertByteBufferToInt8Array")
            println(buffer.getByte(0))
            val pixmap: Pixmap = Pixmap.pixmaps[(pixels as IntBuffer)[0]] ?:throw java.lang.IllegalStateException("Pixmap not fond")
            println(pixmap.imageElement.getSrc())
            // Prefer to use the HTMLImageElement when possible, since reading from the CanvasElement can be lossy.
            println("before pixmap.canUseImageElement()")
            if (pixmap.canUseImageElement()) {
                println("useImageElement")
                delegate.texImage2D(target, level, internalformat, format, type, pixmap.imageElement)
            } else {
                println("useCanvasElement")
                // TODO: use CanvasElement, not Libgdx internal thingie
//                    delegate.texImage2D(target, level, internalformat, format, type, pixmap.getCanvasElement())
            }
//            }
        }
        println("after glTexImage2D")

//        delegate.texImage2D(target, level, internalformat, width, height, border, format, type, convertByteBufferToInt8Array(pixels))
    }

    override fun glVertexAttrib3fv(indx: Int, values: FloatBuffer) {
        delegate.vertexAttrib3fv(indx, convertBufferToFloatArray(values))
    }

    override fun glBlendFunc(sfactor: Int, dfactor: Int) {
        delegate.blendFunc(sfactor, dfactor)
    }

    override fun glIsEnabled(cap: Int): Boolean {
        return delegate.isEnabled(cap)
    }

    override fun glGetAttribLocation(program: Int, name: String): Int {
        return delegate.getAttribLocation(programs.getProgram(program), name)
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
            val shader1 = shaders.getShader(shaderId)


            val ib = BufferUtils.newIntBuffer(1)

            someDep.putBuf(ib)

            println("ib.getttt(0): "+  ib.get(0))


            val fb = FloatBuffer.allocate(1)
            fb.put(3F)
            println("fb.get(0)" + fb.get(0))


            val result: Boolean = delegate.getShaderParameterBoolean(shader1, pname)
            println("glGetShaderiv() - Result: $result")
            val valueToPut = if (result) GL20.GL_TRUE else GL20.GL_FALSE
            println("glGetShaderiv valueToPut: $valueToPut")
            params.put(0, valueToPut)
            println("params.get(0):" + params.get(0))
            println("params hasArray: ${params.hasArray()}")
        } else {
            println("glGetShaderiv not implemented for pname:  $pname")
        }
    }

    override fun glGetUniformfv(program: Int, location: Int, params: FloatBuffer) {
        delegate.getUniformfv(programs.getProgram(program), uniforms.getUniformLocation(location), convertBufferToFloatArray(params))
    }

    override fun glUniform4f(location: Int, x: Float, y: Float, z: Float, w: Float) {
        delegate.uniform4f(uniforms.getUniformLocation(location), x, y, z, w)
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
        if (ptr !is ByteBuffer) {
            println("glVertexAttribPointer data type!")
            return
        }

        delegate.vertexAttribPointer(indx, size, type, normalized, stride, convertByteBufferToInt8Array(ptr))
    }

    override fun glVertexAttribPointer(indx: Int, size: Int, type: Int, normalized: Boolean, stride: Int, ptr: Int) {
        delegate.vertexAttribPointer(indx, size, type, normalized, stride, ptr)
    }

    override fun glStencilMaskSeparate(face: Int, mask: Int) {
        delegate.stencilMaskSeparate(face, mask)
    }

    override fun glDrawElements(mode: Int, count: Int, type: Int, indices: Buffer?) {
        if (indices !is ByteBuffer) {
            println("glCompressedTexImage2D data type!")
            return
        }

        delegate.drawElements(mode, count, type, convertByteBufferToInt8Array(indices))
    }

    override fun glDrawElements(mode: Int, count: Int, type: Int, indices: Int) {
        delegate.drawElements(mode, count, type, indices)
    }

    override fun glTexParameteri(target: Int, pname: Int, param: Int) {
        delegate.texParameteri(target, pname, param)
    }

    override fun glUseProgram(program: Int) {
        delegate.useProgram(programs.getProgram(program))
    }

    override fun glFinish() {
        delegate.finish()
    }

    override fun glGetIntegerv(pname: Int, params: IntBuffer) {
        delegate.getIntegerv(pname, convertIntBufferToIntArray(params))
    }

    override fun glBlendEquation(mode: Int) {
        delegate.blendEquation(mode)
    }

    override fun glUniform4i(location: Int, x: Int, y: Int, z: Int, w: Int) {
        delegate.uniform4i(uniforms.getUniformLocation(location), x, y, z, w)
    }

    override fun glVertexAttrib1fv(indx: Int, values: FloatBuffer) {
        delegate.vertexAttrib1fv(indx, convertBufferToFloatArray(values))
    }

    override fun glUniform3fv(location: Int, count: Int, v: FloatBuffer) {
        delegate.uniform3fv(uniforms.getUniformLocation(location), convertBufferToFloatArray(v))
    }

    override fun glUniform3fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform3fv(uniforms.getUniformLocation(location), convertFloatArray(v))
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
        delegate.getFloatv(pname, convertBufferToFloatArray(params))
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
        if (data !is ByteBuffer) {
            println("glBufferSubData data type!")
            return
        }

        delegate.bufferSubData(target, offset, size, convertByteBufferToInt8Array(data))
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
        delegate.genBuffers(n, convertIntBufferToIntArray(buffers))
    }

    override fun glGetAttachedShaders(program: Int, maxcount: Int, count: Buffer?, shaders: IntBuffer) {
        if (count !is ByteBuffer) {
            println("glGetAttachedShaders data type!")
            return
        }

        delegate.getAttachedShaders(programs.getProgram(program), maxcount, convertByteBufferToInt8Array(count), convertIntBufferToIntArray(shaders))
    }

    override fun glGenRenderbuffers(n: Int, renderbuffers: IntBuffer) {
        delegate.genRenderbuffers(n, convertIntBufferToIntArray(renderbuffers))
    }

    override fun glRenderbufferStorage(target: Int, internalformat: Int, width: Int, height: Int) {
        delegate.renderbufferStorage(target, internalformat, width, height)
    }

    override fun glUniform3f(location: Int, x: Float, y: Float, z: Float) {
        delegate.uniform3f(uniforms.getUniformLocation(location), x, y, z)
    }

    override fun glReadPixels(x: Int, y: Int, width: Int, height: Int, format: Int, type: Int, pixels: Buffer?) {
        if (pixels !is ByteBuffer) {
            println("glReadPixels data type!")
            return
        }

        delegate.readPixels(x, y, width, height, format, type, convertByteBufferToInt8Array(pixels))
    }

    override fun glStencilMask(mask: Int) {
        delegate.stencilMask(mask)
    }

    override fun glBlendFuncSeparate(srcRGB: Int, dstRGB: Int, srcAlpha: Int, dstAlpha: Int) {
        delegate.blendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha)
    }

    override fun glGetShaderPrecisionFormat(shadertype: Int, precisiontype: Int, range: IntBuffer, precision: IntBuffer) {
        delegate.getShaderPrecisionFormat(shadertype, precisiontype, convertIntBufferToIntArray(range), convertIntBufferToIntArray(precision))
    }

    override fun glIsTexture(texture: Int): Boolean {
        return delegate.isTexture(textures.getTexture(texture))
    }

    override fun glLinkProgram(program: Int) {
        delegate.linkProgram(programs.getProgram(program))
    }

    override fun glGetVertexAttribfv(index: Int, pname: Int, params: FloatBuffer) {
        delegate.getVertexAttribfv(index, pname, convertBufferToFloatArray(params))
    }

    override fun glGetVertexAttribPointerv(index: Int, pname: Int, pointer: Buffer?) {
        if (pointer !is ByteBuffer) {
            println("glGetVertexAttribPointerv data type!")
            return
        }

        delegate.getVertexAttribPointerv(index, pname, convertByteBufferToInt8Array(pointer))
    }

    override fun glCreateShader(type: Int): Int {
        println("glCreateShader called")
        val createShader = delegate.createShader(type)
        val shaderId = ++lastCreatedShader
        shaders[shaderId] = createShader
        return shaderId
    }

    override fun glStencilFuncSeparate(face: Int, func: Int, ref: Int, mask: Int) {
        delegate.stencilFuncSeparate(face, func, ref, mask)
    }

    override fun glGetString(name: Int): String {
        return delegate.getString(name)
    }

    override fun glCompressedTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: Buffer?) {
        if (data !is ByteBuffer) {
            println("glCompressedTexImage2D data type!")
            return
        }

        delegate.compressedTexImage2D(target, level, internalformat, width, height, border, imageSize, convertByteBufferToInt8Array(data))
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
        println("convertBufferToFloatArray")
        val buffer = inputBuffer.duplicate()

        // TODO #2 rewind should not be there, but the source buffer is already navigated to the end.
        buffer.rewind()

        val result = OpaqueArrays.createFloatArray(buffer.remaining())
        val tmp: FloatArray
        if (buffer.hasArray()) {
            tmp = buffer.array()
        } else {
            tmp = FloatArray(buffer.remaining())
            buffer[tmp]
        }
        for (i in tmp.indices) {
            println(" $i -  - ${tmp[i]}")
            result.setFloat(i, tmp[i])
        }
        println("Return convertBufferToFloatArray, result length: ${result.floatArrayLength()}")
        return result
    }

    override fun glUniform2iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform2iv(uniforms.getUniformLocation(location), convertIntBufferToIntArray(v))
    }

    override fun glUniform2iv(location: Int, count: Int, v: kotlin.IntArray, offset: Int) {
        val intArray: IntArray = intArrayToBytecoderIntArray(v)
        delegate.uniform2iv(uniforms.getUniformLocation(location), intArray)
    }

    override fun glGenBuffer(): Int {
        val createBuffer = delegate.createBuffer()
        val bufferId = ++lastCreatedBuffer
        buffers[bufferId] = createBuffer

        return bufferId
    }

    override fun glEnable(cap: Int) {
        delegate.enable(cap)
    }

    override fun glGetUniformiv(program: Int, location: Int, params: IntBuffer) {
        delegate.getUniformiv(programs.getProgram(program), uniforms.getUniformLocation(location), convertIntBufferToIntArray(params))
    }

    override fun glGetFramebufferAttachmentParameteriv(target: Int, attachment: Int, pname: Int, params: IntBuffer) {
        delegate.getFramebufferAttachmentParameteriv(target, attachment, pname, convertIntBufferToIntArray(params))
    }

    override fun glDeleteRenderbuffer(renderbuffer: Int) {
        val renderBuffer = renderBuffers.remove(renderbuffer)
        if (renderBuffer == null) {
            throw makeAndLogIllegalArgumentException(tag, "RenderBuffer not found: $renderBuffer")
        }
        delegate.deleteRenderbuffer(renderBuffer)
    }

    override fun glGenFramebuffers(n: Int, framebuffers: IntBuffer) {
        delegate.genFramebuffers(n, convertIntBufferToIntArray(framebuffers))
    }

}

fun MutableMap<Int, WebGLShader>.getShader(shaderId: Int) : WebGLShader =
        get(shaderId)?: throw IllegalStateException("Shader not found: $shaderId")

fun MutableMap<Int, WebGLProgram>.getProgram(programId: Int): WebGLProgram =
        get(programId)?: throw IllegalStateException("Program not found: $programId")

fun MutableMap<Int, WebGLBuffer>.getBuffer(bufferId: Int): WebGLBuffer =
        get(bufferId)?: throw IllegalStateException("Buffer not found: $bufferId")

fun MutableMap<Int, WebGLUniformLocation>.getUniformLocation(uniformLocationId: Int): WebGLUniformLocation =
        get(uniformLocationId) ?: throw makeAndLogIllegalArgumentException(tag, "getUniformLocation($uniformLocationId) " +
                "failed: no location for uniformLocationId: $uniformLocationId")

fun MutableMap<Int, WebGLRenderBuffer>.getRenderBuffer(renderBufferId: Int): WebGLRenderBuffer =
        get(renderBufferId) ?: throw makeAndLogIllegalArgumentException(tag, "getRenderBuffer($renderBufferId) " +
                "failed: no renderBuffer for renderBufferId: $renderBufferId")

fun MutableMap<Int, WebGLFrameBuffer>.getFrameBuffer(frameBufferId: Int): WebGLFrameBuffer =
        get(frameBufferId) ?: throw makeAndLogIllegalArgumentException(tag, "getFrameBuffer($frameBufferId) " +
                "failed: no frameBuffer for frameBufferId: $frameBufferId")

fun MutableMap<Int, WebGLTexture>.getTexture(textureId: Int): WebGLTexture =
        get(textureId) ?: throw makeAndLogIllegalArgumentException(tag, "getTexture($textureId) " +
                "failed: no texture for textureId: $textureId")