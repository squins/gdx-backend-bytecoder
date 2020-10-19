package bytecoder

import com.badlogic.gdx.graphics.GL20
import de.mirkosertic.bytecoder.api.web.Int8Array
import de.mirkosertic.bytecoder.api.web.IntArray
import de.mirkosertic.bytecoder.api.web.OpaqueArrays
import ext.*
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer


class BytecoderGL20(private val delegate: WebGLRenderingContext) : GL20 {

    private var lastCreatedShader:Int = 0
    private var shaders: MutableMap<Int, WebGLShader> = mutableMapOf()

    private var lastCreatedProgram: Int = 0
    private var programs: MutableMap<Int, WebGLProgram> = mutableMapOf()

    private var lastCreatedBuffer: Int = 0
    private var buffers: MutableMap<Int, WebGLBuffer> = mutableMapOf()

    private var lastCreatedUniformLocation: Int = 0
    private var uniformLocations: MutableMap<Int, WebGLUniformLocation> = mutableMapOf()

    override fun glUniform3i(location: Int, x: Int, y: Int, z: Int) {
        delegate.uniform3i(location, x, y, z)
    }

    override fun glLineWidth(width: Float) {
        delegate.lineWidth(width)
    }

    override fun glDeleteShader(shaderId: Int) {

        val shader = shaders.remove(shaderId)
        if (shader == null) {
            throw IllegalStateException("Shader not found: $shader")
        }
        delegate.deleteShader(shader)
    }

    override fun glDetachShader(program: Int, shader: Int) {
        delegate.detachShader(program, shader)
    }

    override fun glVertexAttrib3f(indx: Int, x: Float, y: Float, z: Float) {
        delegate.vertexAttrib3f(indx, x, y, z)
    }

    override fun glCompileShader(shader: Int) {
        delegate.compileShader(shaders.getShader(shader))
    }

    override fun glTexParameterfv(target: Int, pname: Int, params: FloatBuffer?) {
        delegate.texParameterfv(target, pname, params)
    }

    override fun glStencilFunc(func: Int, ref: Int, mask: Int) {
        delegate.stencilFunc(func, ref, mask)
    }

    override fun glDeleteFramebuffer(framebuffer: Int) {
        delegate.deleteFramebuffer(framebuffer)
    }

    override fun glGenTexture(): Int {
        return delegate.genTexture()
    }

    override fun glBindAttribLocation(program: Int, index: Int, name: String?) {
        delegate.bindAttribLocation(program, index, name)
    }

    override fun glEnableVertexAttribArray(index: Int) {
        delegate.enableVertexAttribArray(index)
    }

    override fun glReleaseShaderCompiler() {
        delegate.releaseShaderCompiler()
    }

    override fun glUniform2f(location: Int, x: Float, y: Float) {
        delegate.uniform2f(location, x, y)
    }

    override fun glGetActiveAttrib(program: Int, index: Int, size: IntBuffer, type: IntBuffer): String {
        return delegate.getActiveAttrib(program, index, convertIntBufferToIntArray(size), convertIntBufferToIntArray(type))
    }

    override fun glGenFramebuffer(): Int {
        return delegate.genFramebuffer()
    }

    override fun glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer?) {
        delegate.uniformMatrix2fv(location, count, transpose, value)
    }

    override fun glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int) {
        delegate.uniformMatrix2fv(location, count, transpose, convertFloatArray(value), offset)
    }

    override fun glUniform2fv(location: Int, count: Int, v: FloatBuffer?) {
        delegate.uniform2fv(location, count, v)
    }

    override fun glUniform2fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform2fv(location, count, convertFloatArray(v), offset)
    }

    override fun glUniform4iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform4iv(location, count, convertIntBufferToIntArray(v))
    }

    override fun glUniform4iv(location: Int, count: Int, v: kotlin.IntArray?, offset: Int) {
        delegate.uniform4iv(location, count, v, offset)
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
        delegate.getProgramiv(program, pname, convertIntBufferToIntArray(params))
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

    override fun glVertexAttrib4fv(indx: Int, values: FloatBuffer?) {
        delegate.vertexAttrib4fv(indx, values)
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
        delegate.uniform2i(location, x, y)
    }

    override fun glCheckFramebufferStatus(target: Int): Int {
        return delegate.checkFramebufferStatus(target)
    }

    override fun glDeleteTextures(n: Int, textures: IntBuffer) {
        delegate.deleteTextures(n, convertIntBufferToIntArray(textures))
    }

    override fun glBindRenderbuffer(target: Int, renderbuffer: Int) {
        delegate.bindRenderbuffer(target, renderbuffer)
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
        return delegate.getProgramInfoLog(program)
    }

    override fun glIsRenderbuffer(renderbuffer: Int): Boolean {
        return delegate.isRenderbuffer(renderbuffer)
    }

    override fun glFrontFace(mode: Int) {
        delegate.frontFace(mode)
    }

    override fun glUniform1iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform1iv(location, count, convertIntBufferToIntArray(v))
    }

    override fun glUniform1iv(location: Int, count: Int, v: kotlin.IntArray?, offset: Int) {
        delegate.uniform1iv(location, count, v, offset)
    }

    override fun glBindTexture(target: Int, texture: Int) {
        delegate.bindTexture(target, texture)
    }

    override fun glGetUniformLocation(program: Int, name: String): Int {
        var uniformLocationId = 0
        val getUniformLocation = delegate.getUniformLocation(programs.getProgram(program), name)
        uniformLocations.forEach{
            if (it.value == getUniformLocation) {
            uniformLocationId = it.key
            }
        }
        return uniformLocationId
    }

    override fun glPixelStorei(pname: Int, param: Int) {
        delegate.pixelStorei(pname, param)
    }

    override fun glHint(target: Int, mode: Int) {
        delegate.hint(target, mode)
    }

    override fun glFramebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: Int) {
        delegate.framebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer)
    }

    override fun glUniform1f(location: Int, x: Float) {
        delegate.uniform1f(location, x)
    }

    override fun glDepthMask(flag: Boolean) {
        delegate.depthMask(flag)
    }

    override fun glBlendColor(red: Float, green: Float, blue: Float, alpha: Float) {
        delegate.blendColor(red, green, blue, alpha)
    }

    override fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer) {
        delegate.uniformMatrix4fv(uniformLocations.getUniformLocation(location), transpose, convertBufferToFloatArray(value))
    }

    override fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int) {
        delegate.uniformMatrix4fv(uniformLocations.getUniformLocation(location), count, transpose, convertFloatArray(value), offset)
    }

    override fun glBufferData(target: Int, size: Int, data: Buffer, usage: Int) {
        //TODO is it ok to ignore sizes? (check GWT)
        delegate.bufferData(target, convertBufferToFloatArray(data), usage)
    }


    override fun glValidateProgram(program: Int) {
        delegate.validateProgram(program)
    }

    override fun glTexParameterf(target: Int, pname: Int, param: Float) {
        delegate.texParameterf(target, pname, param)
    }

    override fun glIsFramebuffer(framebuffer: Int): Boolean {
        return delegate.isFramebuffer(framebuffer)
    }

    override fun glDeleteBuffer(buffer: Int) {
        delegate.deleteBuffer(buffer)
    }

    override fun glShaderSource(shaderId: Int, sourceCode: String) {
        delegate.shaderSource(shaders.getShader(shaderId), sourceCode)
    }

    override fun glVertexAttrib2fv(indx: Int, values: FloatBuffer?) {
        delegate.vertexAttrib2fv(indx, values)
    }

    override fun glDeleteFramebuffers(n: Int, framebuffers: IntBuffer) {
        delegate.deleteFramebuffers(n, convertIntBufferToIntArray(framebuffers))
    }

    override fun glUniform4fv(location: Int, count: Int, v: FloatBuffer?) {
        delegate.uniform4fv(location, count, v)
    }

    override fun glUniform4fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform4fv(location, count, convertFloatArray(v), offset)
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

    override fun glDeleteProgram(program: Int) {
        delegate.deleteProgram(program)
    }

    override fun glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int) {
        delegate.framebufferTexture2D(target, attachment, textarget, texture, level)
    }

    override fun glGenRenderbuffer(): Int {
        return delegate.genRenderbuffer()
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
        return delegate.getShaderInfoLog(shader)
    }

    override fun glGetActiveUniform(program: Int, index: Int, size: IntBuffer, type: IntBuffer): String {
        return delegate.getActiveUniform(program, index, convertIntBufferToIntArray(size), convertIntBufferToIntArray(type))
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
        delegate.uniform1i(location, x)
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
        programs.put(programId, createProgram)

        return programId
    }

    override fun glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer?) {
        delegate.uniformMatrix3fv(location, count, transpose, value)
    }

    override fun glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int) {
        delegate.uniformMatrix3fv(location, count, transpose, convertFloatArray(value), offset)
    }

    override fun glGetTexParameterfv(target: Int, pname: Int, params: FloatBuffer?) {
        delegate.getTexParameterfv(target, pname, params)
    }

    override fun glVertexAttrib1f(indx: Int, x: Float) {
        delegate.vertexAttrib1f(indx, x)
    }

    override fun glUniform1fv(location: Int, count: Int, v: FloatBuffer?) {
        delegate.uniform1fv(location, count, v)
    }

    override fun glUniform1fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform1fv(location, count, convertFloatArray(v), offset)
    }

    override fun glUniform3iv(location: Int, count: Int, v: IntBuffer) {
        delegate.uniform3iv(location, count, convertIntBufferToIntArray(v))
    }

    override fun glUniform3iv(location: Int, count: Int, v: kotlin.IntArray?, offset: Int) {
        delegate.uniform3iv(location, count, v, offset)
    }

    override fun glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: Buffer?) {
        if (pixels !is ByteBuffer) {
            println("glTexImage2D data type!")
            return
        }

        delegate.texImage2D(target, level, internalformat, width, height, border, format, type, convertByteBufferToInt8Array(pixels))
    }

    override fun glVertexAttrib3fv(indx: Int, values: FloatBuffer?) {
        delegate.vertexAttrib3fv(indx, values)
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

    override fun glGetShaderiv(shader: Int, pname: Int, params: IntBuffer) {
        delegate.getShaderiv(shader, pname, convertIntBufferToIntArray(params))
    }

    override fun glGetUniformfv(program: Int, location: Int, params: FloatBuffer?) {
        delegate.getUniformfv(program, location, params)
    }

    override fun glUniform4f(location: Int, x: Float, y: Float, z: Float, w: Float) {
        delegate.uniform4f(location, x, y, z, w)
    }

    override fun glClear(mask: Int) {
        delegate.clear(mask)
    }

    override fun glDepthFunc(func: Int) {
        delegate.depthFunc(func)
    }

    override fun glIsBuffer(buffer: Int): Boolean {
        return delegate.isBuffer(buffer)
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
        delegate.uniform4i(location, x, y, z, w)
    }

    override fun glVertexAttrib1fv(indx: Int, values: FloatBuffer?) {
        delegate.vertexAttrib1fv(indx, values)
    }

    override fun glUniform3fv(location: Int, count: Int, v: FloatBuffer?) {
        delegate.uniform3fv(location, count, v)
    }

    override fun glUniform3fv(location: Int, count: Int, v: FloatArray, offset: Int) {
        delegate.uniform3fv(location, count, convertFloatArray(v), offset)
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

    override fun glGetFloatv(pname: Int, params: FloatBuffer?) {
        delegate.getFloatv(pname, params)
    }

    override fun glDrawArrays(mode: Int, first: Int, count: Int) {
        delegate.drawArrays(mode, first, count)
    }

    override fun glBindFramebuffer(target: Int, framebuffer: Int) {
        delegate.bindFramebuffer(target, framebuffer)
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
        return delegate.isProgram(program)
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

        delegate.getAttachedShaders(program, maxcount, convertByteBufferToInt8Array(count), convertIntBufferToIntArray(shaders))
    }

    override fun glGenRenderbuffers(n: Int, renderbuffers: IntBuffer) {
        delegate.genRenderbuffers(n, convertIntBufferToIntArray(renderbuffers))
    }

    override fun glRenderbufferStorage(target: Int, internalformat: Int, width: Int, height: Int) {
        delegate.renderbufferStorage(target, internalformat, width, height)
    }

    override fun glUniform3f(location: Int, x: Float, y: Float, z: Float) {
        delegate.uniform3f(location, x, y, z)
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
        return delegate.isTexture(texture)
    }

    override fun glLinkProgram(program: Int) {
        delegate.linkProgram(programs.getProgram(program))
    }

    override fun glGetVertexAttribfv(index: Int, pname: Int, params: FloatBuffer?) {
        delegate.getVertexAttribfv(index, pname, params)
    }

    override fun glGetVertexAttribPointerv(index: Int, pname: Int, pointer: Buffer?) {
        if (pointer !is ByteBuffer) {
            println("glGetVertexAttribPointerv data type!")
            return
        }

        delegate.getVertexAttribPointerv(index, pname, convertByteBufferToInt8Array(pointer))
    }

    override fun glCreateShader(type: Int): Int {
        val createShader = delegate.createShader(type)
        val shaderId = ++lastCreatedShader
        shaders.put(shaderId, createShader)

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
            throw IllegalArgumentException("Sorry data type is not supported")
        }

        println("convertBufferToFloatArray data: $data , hasArray: ${data.hasArray()}")
        val arrayObject = data.array()
        println("arrayObject: $arrayObject")
//        val array = data.array() as Array<Any>
        val array = data.array() as FloatArray
        println("result array: $array ")
        println("result array size: ${array.size} ")
        array.forEachIndexed { index, value -> println("Index + value: $index + $value")  }
        println("result array at 1: ${array.get(1)} ")

        println("convertBufferToFloatArray dataFloatArray ")
        val dataFloatArray = OpaqueArrays.createFloatArray(array.size)

        println("convertBufferToFloatArray array.size:  ")
        for ((index, value) in array.withIndex()){
            println("in loop")
            println("index: $index")
            println("Value==null: ${value == null}")
            println("Value type: ${value.javaClass}")
            println("value: $value")
            println("convertBufferToFloatArray $index $value")
            dataFloatArray.setFloat(index, value.toFloat())
        }
        return dataFloatArray
    }

    private fun convertBufferToFloatArray(buffer: FloatBuffer): de.mirkosertic.bytecoder.api.web.FloatArray {

        val floatArray = OpaqueArrays.createFloatArray(buffer.limit() - buffer.position())
        var i = buffer.position()
        var j = 0
        while (i < buffer.limit()) {
            floatArray.setFloat(j, buffer[i])
            i++
            j++
        }

        return floatArray;
    }


    private fun convertFloatArray(floatArray: FloatArray): de.mirkosertic.bytecoder.api.web.FloatArray {
        val floatArr = OpaqueArrays.createFloatArray(floatArray.size)

        for((index, value) in floatArray.withIndex()){
            floatArr.setFloat(index, value)
        }
        return floatArr
    }

    override fun glUniform2iv(location: Int, count: Int, v: IntBuffer?) {
        delegate.uniform2iv(location, count, v)
    }

    override fun glUniform2iv(location: Int, count: Int, v: kotlin.IntArray?, offset: Int) {
        delegate.uniform2iv(location, count, v, offset)
    }

    override fun glGenBuffer(): Int {
        val createBuffer = delegate.createBuffer()
        val bufferId = ++lastCreatedBuffer
        buffers.put(bufferId, createBuffer)

        return bufferId
    }

    override fun glEnable(cap: Int) {
        delegate.enable(cap)
    }

    override fun glGetUniformiv(program: Int, location: Int, params: IntBuffer?) {
        delegate.getUniformiv(program, location, params)
    }

    override fun glGetFramebufferAttachmentParameteriv(target: Int, attachment: Int, pname: Int, params: IntBuffer?) {
        delegate.getFramebufferAttachmentParameteriv(target, attachment, pname, params)
    }

    override fun glDeleteRenderbuffer(renderbuffer: Int) {
        delegate.deleteRenderbuffer(renderbuffer)
    }

    override fun glGenFramebuffers(n: Int, framebuffers: IntBuffer?) {
        delegate.genFramebuffers(n, framebuffers)
    }

}

fun MutableMap<Int, WebGLShader>.getShader(shaderId: Int) :WebGLShader =
        get(shaderId)?: throw IllegalStateException("Shader not found: $shaderId")

fun MutableMap<Int, WebGLProgram>.getProgram(programId: Int): WebGLProgram =
        get(programId)?: throw IllegalStateException("Program not found: $programId")

fun MutableMap<Int, WebGLBuffer>.getBuffer(bufferId: Int): WebGLBuffer =
        get(bufferId)?: throw IllegalStateException("Buffer not found: $bufferId")

fun MutableMap<Int, WebGLUniformLocation>.getUniformLocation(uniformLocationId: Int): WebGLUniformLocation =
        get(uniformLocationId)?: throw IllegalStateException("UniformLocation not found: $uniformLocationId")