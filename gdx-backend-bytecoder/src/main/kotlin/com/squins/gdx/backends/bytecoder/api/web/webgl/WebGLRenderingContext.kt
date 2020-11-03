package com.squins.gdx.backends.bytecoder.api.web.webgl

import com.squins.gdx.backends.bytecoder.api.web.HtmlImageElement
import de.mirkosertic.bytecoder.api.OpaqueMethod
import de.mirkosertic.bytecoder.api.OpaqueReferenceType
import de.mirkosertic.bytecoder.api.web.*
import de.mirkosertic.bytecoder.api.web.FloatArray
import de.mirkosertic.bytecoder.api.web.IntArray

interface WebGLRenderingContext : OpaqueReferenceType {

    fun clear(mask: Int)

    fun clearDepth(depth: Float)

    fun clearColor(red: Float, blue: Float, green: Float, alpha: Float)

    fun uniform3i(location: WebGLUniformLocation, x: Int, y: Int, z: Int)

    fun lineWidth(width: Float) 

    fun deleteShader(shader: WebGLShader)

    fun detachShader(program: WebGLProgram, shader: WebGLShader)

    fun vertexAttrib3f(indx: Int, x: Float, y: Float, z: Float) 

    fun compileShader(shader: WebGLShader)

    fun texParameterfv(target: Int, pname: Int, params: FloatArray)

    fun stencilFunc(func: Int, ref: Int, mask: Int) 

    fun deleteFramebuffer(framebuffer: WebGLFrameBuffer)

    fun createTexture(): WebGLTexture

    fun bindAttribLocation(program: WebGLProgram, index: Int, name: String?)

    fun enableVertexAttribArray(index: Int) 

    fun releaseShaderCompiler() 

    fun uniform2f(location: WebGLUniformLocation, x: Float, y: Float)

    fun getActiveAttrib(program: WebGLProgram, index: Int): WebGLActiveInfo

    fun getActiveUniform(program: WebGLProgram, index: Int): WebGLActiveInfo

    fun getActiveAttrib(program: WebGLProgram, index: Int, size: IntArray, type: IntArray): String

    fun createFramebuffer(): WebGLFrameBuffer

    fun uniformMatrix2fv(location: WebGLUniformLocation,transpose: Boolean, value: FloatArray)

    fun uniformMatrix2fv(location: WebGLUniformLocation, count: Int, transpose: Boolean, value: FloatArray?, offset: Int)

    fun uniform2fv(location: WebGLUniformLocation, v: FloatArray)

    fun uniform4iv(location: WebGLUniformLocation, v: IntArray)

    fun colorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) 

    fun polygonOffset(factor: Float, units: Float) 

    fun viewport(x: Int, y: Int, width: Int, height: Int) 

    fun getProgramiv(program: WebGLProgram, pname: Int, params: IntArray)

    fun getBooleanv(pname: Int, params: Int8Array)

    fun getBufferParameteriv(target: Int, pname: Int, params: IntArray)

    fun deleteTexture(texture: WebGLTexture)

    fun getVertexAttribiv(index: Int, pname: Int, params: IntArray)

    fun vertexAttrib4fv(indx: Int, values: FloatArray)

    fun texSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, type: Int, pixels: Int8Array)

    fun texSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, format: Int, type: Int, htmlCanvasElement: HTMLCanvasElement)

    fun deleteRenderbuffers(n: Int, renderbuffers: IntArray)

    fun getTexParameteriv(target: Int, pname: Int, params: IntArray)

    fun genTextures(n: Int, textures: IntArray)

    fun stencilOpSeparate(face: Int, fail: Int, zfail: Int, zpass: Int) 

    fun uniform2i(location: WebGLUniformLocation, x: Int, y: Int) 

    fun checkFramebufferStatus(target: Int): Int 

    fun deleteTextures(n: Int, textures: IntArray)

    fun bindRenderbuffer(target: Int, renderbuffer: WebGLRenderBuffer)

    fun texParameteriv(target: Int, pname: Int, params: IntArray)

    fun vertexAttrib4f(indx: Int, x: Float, y: Float, z: Float, w: Float) 

    fun deleteBuffers(n: Int, buffers: IntArray)

    fun getProgramInfoLog(program: WebGLProgram): String

    fun isRenderbuffer(renderbuffer: WebGLRenderBuffer): Boolean

    fun frontFace(mode: Int) 

    fun uniform1iv(location: WebGLUniformLocation, count: Int, v: IntArray)

    fun uniform1iv(location: WebGLUniformLocation, v: IntArray)

    fun uniform1iv(location: WebGLUniformLocation, count: Int, v: kotlin.IntArray, offset: Int)

    fun bindTexture(target: Int, texture: WebGLTexture)

    fun getUniformLocation(program: WebGLProgram, name: String): WebGLUniformLocation

    fun pixelStorei(pname: Int, param: Int) 

    fun hint(target: Int, mode: Int) 

    fun framebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: WebGLRenderBuffer)

    fun uniform1f(location: WebGLUniformLocation, x: Float) 

    fun depthMask(flag: Boolean) 

    fun blendColor(red: Float, green: Float, blue: Float, alpha: Float) 

    fun uniformMatrix4fv(location: WebGLUniformLocation, transpose: Boolean, value: FloatArray)

    fun bufferData(target: Int, data: FloatArray, usage: Int)

    fun bufferData(target: Int, data: Int16Array, usage: Int)

    fun validateProgram(program: WebGLProgram)

    fun texParameterf(target: Int, pname: Int, param: Float) 

    fun isFramebuffer(framebuffer: WebGLFrameBuffer): Boolean

    fun deleteBuffer(buffer: WebGLBuffer)

    fun shaderSource(shader: WebGLShader, sourcecode: String)

    fun vertexAttrib2fv(indx: Int, values: FloatArray)

    fun deleteFramebuffers(n: Int, framebuffers: IntArray)

    fun uniform4fv(location: WebGLUniformLocation, v: FloatArray)

    fun compressedTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, imageSize: Int, data: Int8Array)

    fun generateMipmap(target: Int) 

    fun deleteProgram(program: WebGLProgram)

    fun framebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: WebGLTexture, level: Int)

    fun createRenderbuffer(): WebGLRenderBuffer

    fun attachShader(program: WebGLProgram, shader: WebGLShader)

    fun bindBuffer(target: Int, buffer: WebGLBuffer)

    fun shaderBinary(n: Int, shaders: IntArray, binaryformat: Int, binary: Int8Array, length: Int)

    fun disable(cap: Int) 

    fun getRenderbufferParameteriv(target: Int, pname: Int, params: IntArray)

    fun getShaderInfoLog(shader: WebGLShader): String

    fun getActiveUniform(program: WebGLProgram, index: Int, size: IntArray, type: IntArray): String

    fun isShader(shader: Int): Boolean 

    fun uniform1i(location: WebGLUniformLocation, x: Int) 

    fun blendEquationSeparate(modeRGB: Int, modeAlpha: Int) 

    fun scissor(x: Int, y: Int, width: Int, height: Int) 

    fun createProgram(): WebGLProgram

    fun uniformMatrix3fv(location: WebGLUniformLocation, transpose: Boolean, value: FloatArray)

    fun getTexParameterfv(target: Int, pname: Int, params: FloatArray)

    fun getTexParameter(target: Int, pname: Int)

    fun vertexAttrib1f(indx: Int, x: Float) 

    fun uniform1fv(location: WebGLUniformLocation, v: FloatArray)

    fun uniform3iv(location: WebGLUniformLocation, v: IntArray)

    fun texImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: Int8Array?)

    fun texImage2D(target: Int, level: Int, internalformat: Int, format: Int, type: Int, canvas: HTMLCanvasElement)

    fun texImage2D(target: Int, level: Int, internalformat: Int, format: Int, type: Int, image: HtmlImageElement)

    fun vertexAttrib3fv(indx: Int, values: FloatArray)

    fun blendFunc(sfactor: Int, dfactor: Int) 

    fun isEnabled(cap: Int): Boolean 

    fun getAttribLocation(program: WebGLProgram, name: String): Int

    fun depthRangef(zNear: Float, zFar: Float) 

    fun flush() 

    fun sampleCoverage(value: Float, invert: Boolean) 

    fun copyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int) 

    fun getShaderiv(shader: WebGLShader, pname: Int, params: IntArray)

    fun getUniformfv(program: WebGLProgram, location: WebGLUniformLocation, params: FloatArray)

    fun uniform4f(location: WebGLUniformLocation, x: Float, y: Float, z: Float, w: Float)

    fun depthFunc(func: Int) 

    fun isBuffer(buffer: WebGLBuffer): Boolean

    fun vertexAttribPointer(indx: Int, size: Int, type: Int, normalized: Boolean, stride: Int, ptr: Int8Array?)

    fun vertexAttribPointer(indx: Int, size: Int, type: Int, normalized: Boolean, stride: Int, ptr: Int) 

    fun stencilMaskSeparate(face: Int, mask: Int) 

    fun drawElements(mode: Int, count: Int, type: Int, indices: Int8Array?)

    fun drawElements(mode: Int, count: Int, type: Int, indices: Int) 

    fun texParameteri(target: Int, pname: Int, param: Int) 

    fun useProgram(program: WebGLProgram)

    fun finish() 

    fun getIntegerv(pname: Int, params: IntArray)

    fun blendEquation(mode: Int) 

    fun uniform4i(location: WebGLUniformLocation, x: Int, y: Int, z: Int, w: Int) 

    fun vertexAttrib1fv(indx: Int, values: FloatArray)

    fun uniform3fv(location: WebGLUniformLocation, v: FloatArray)

    fun vertexAttrib2f(indx: Int, x: Float, y: Float) 

    fun activeTexture(texture: Int)

    fun cullFace(mode: Int) 

    fun clearStencil(s: Int) 

    fun getFloatv(pname: Int, params: FloatArray)

    fun drawArrays(mode: Int, first: Int, count: Int) 

    fun bindFramebuffer(target: Int, framebuffer: WebGLFrameBuffer)

    fun getError(): Int 

    fun bufferSubData(target: Int, offset: Int, size: Int, data: Int8Array)

    fun bufferSubData(target: Int, offset: Int, data: FloatArray)

    fun bufferSubData(target: Int, offset: Int, data: Int16Array)

    fun copyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int) 

    fun isProgram(program: WebGLProgram): Boolean

    fun stencilOp(fail: Int, zfail: Int, zpass: Int) 

    fun disableVertexAttribArray(index: Int) 

    fun genBuffers(n: Int, buffers: IntArray)

    fun getAttachedShaders(program: WebGLProgram, maxcount: Int, count: Int8Array, shaders: IntArray)

    fun genRenderbuffers(n: Int, renderbuffers: IntArray)

    fun renderbufferStorage(target: Int, internalformat: Int, width: Int, height: Int) 

    fun uniform3f(location: WebGLUniformLocation, x: Float, y: Float, z: Float) 

    fun readPixels(x: Int, y: Int, width: Int, height: Int, format: Int, type: Int, pixels: Int8Array)

    fun stencilMask(mask: Int) 

    fun blendFuncSeparate(srcRGB: Int, dstRGB: Int, srcAlpha: Int, dstAlpha: Int) 

    fun getShaderPrecisionFormat(shadertype: Int, precisiontype: Int, range: IntArray, precision: IntArray)

    fun isTexture(texture: WebGLTexture): Boolean

    fun getVertexAttribfv(index: Int, pname: Int, params: FloatArray)

    fun getVertexAttribPointerv(index: Int, pname: Int, pointer: Int8Array)

    fun createShader(type: Int): WebGLShader

    fun stencilFuncSeparate(face: Int, func: Int, ref: Int, mask: Int) 

    fun getString(name: Int): String 

    fun compressedTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: Int8Array)

    fun uniform2iv(location: WebGLUniformLocation, v: IntArray)

    fun createBuffer(): WebGLBuffer

    fun enable(cap: Int) 

    fun getUniformiv(program: WebGLProgram, location: WebGLUniformLocation, params: IntArray)

    fun getFramebufferAttachmentParameteriv(target: Int, attachment: Int, pname: Int, params: IntArray)

    fun deleteRenderbuffer(renderbuffer: WebGLRenderBuffer)

    fun genFramebuffers(n: Int, framebuffers: IntArray)

    fun linkProgram(program: WebGLProgram)

    fun getParameter(pname: Int)

    fun getShaderParameteri(shader: WebGLShader, pname: Int) : Int

    @OpaqueMethod("getParameter")
    fun getParameterf(pname: Int) : Float

    @OpaqueMethod("getParameter")
    fun getParameteri(pname: Int) : Int

    @OpaqueMethod("getParameter")
    fun getParameterb(pname: Int) : Boolean

    @OpaqueMethod("getParameter")
    fun getParameterString(pname: Int) : String

    @OpaqueMethod("getShaderParameter")
    fun getShaderParameterBoolean(shader: WebGLShader, pname: Int) : Boolean

    @OpaqueMethod("getShaderParameter")
    fun getShaderParameterInt(shader: WebGLShader, pname: Int) : Int

    @OpaqueMethod("getProgramParameter")
    fun getProgramParameterBoolean(program: WebGLProgram, pname: Int): Boolean

    @OpaqueMethod("getProgramParameter")
    fun getProgramParameterInt(program: WebGLProgram, pname: Int): Int
}

