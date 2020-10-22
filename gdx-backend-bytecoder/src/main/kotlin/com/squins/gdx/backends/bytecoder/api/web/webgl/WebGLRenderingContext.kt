package com.squins.gdx.backends.bytecoder.api.web.webgl

import de.mirkosertic.bytecoder.api.OpaqueMethod
import de.mirkosertic.bytecoder.api.OpaqueReferenceType
import de.mirkosertic.bytecoder.api.web.Int8Array
import de.mirkosertic.bytecoder.api.web.IntArray
import de.mirkosertic.bytecoder.api.web.FloatArray
import java.nio.FloatBuffer
import java.nio.IntBuffer


interface WebGLRenderingContext : OpaqueReferenceType {

    fun clear(mask: Int)

    fun clearDepth(depth: Float)

    fun clearColor(red: Float, blue: Float, green: Float, alpha: Float)

    fun uniform3i(location: Int, x: Int, y: Int, z: Int)

    fun lineWidth(width: Float) 

    fun deleteShader(shader: WebGLShader)

    fun detachShader(program: Int, shader: Int) 

    fun vertexAttrib3f(indx: Int, x: Float, y: Float, z: Float) 

    fun compileShader(shader: WebGLShader)

    fun texParameterfv(target: Int, pname: Int, params: FloatBuffer?)

    fun stencilFunc(func: Int, ref: Int, mask: Int) 

    fun deleteFramebuffer(framebuffer: Int) 

    fun genTexture(): Int 

    fun bindAttribLocation(program: Int, index: Int, name: String?) 

    fun enableVertexAttribArray(index: Int) 

    fun releaseShaderCompiler() 

    fun uniform2f(location: Int, x: Float, y: Float) 

    fun getActiveAttrib(program: Int, index: Int, size: IntArray, type: IntArray): String

    fun genFramebuffer(): Int 

    fun uniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer?) 

    fun uniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatArray?, offset: Int)

    fun uniform2fv(location: Int, count: Int, v: FloatBuffer?) 

    fun uniform2fv(location: Int, count: Int, v: FloatArray?, offset: Int) 

    fun uniform4iv(location: Int, count: Int, v: IntArray)

    fun uniform4iv(location: Int, count: Int, v: kotlin.IntArray?, offset: Int)

    fun colorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) 

    fun polygonOffset(factor: Float, units: Float) 

    fun viewport(x: Int, y: Int, width: Int, height: Int) 

    fun getProgramiv(program: Int, pname: Int, params: IntArray)

    fun getBooleanv(pname: Int, params: Int8Array)

    fun getBufferParameteriv(target: Int, pname: Int, params: IntArray)

    fun deleteTexture(texture: Int) 

    fun getVertexAttribiv(index: Int, pname: Int, params: IntArray)

    fun vertexAttrib4fv(indx: Int, values: FloatBuffer?) 

    fun texSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, type: Int, pixels: Int8Array)

    fun deleteRenderbuffers(n: Int, renderbuffers: IntArray)

    fun getTexParameteriv(target: Int, pname: Int, params: IntArray)

    fun genTextures(n: Int, textures: IntArray)

    fun stencilOpSeparate(face: Int, fail: Int, zfail: Int, zpass: Int) 

    fun uniform2i(location: Int, x: Int, y: Int) 

    fun checkFramebufferStatus(target: Int): Int 

    fun deleteTextures(n: Int, textures: IntArray)

    fun bindRenderbuffer(target: Int, renderbuffer: Int) 

    fun texParameteriv(target: Int, pname: Int, params: IntArray)

    fun vertexAttrib4f(indx: Int, x: Float, y: Float, z: Float, w: Float) 

    fun deleteBuffers(n: Int, buffers: IntArray)

    fun getProgramInfoLog(program: Int): String 

    fun isRenderbuffer(renderbuffer: Int): Boolean 

    fun frontFace(mode: Int) 

    fun uniform1iv(location: Int, count: Int, v: IntArray)

    fun uniform1iv(location: Int, count: Int, v: kotlin.IntArray?, offset: Int)

    fun bindTexture(target: Int, texture: Int) 

    fun getUniformLocation(program: WebGLProgram, name: String): WebGLUniformLocation

    fun pixelStorei(pname: Int, param: Int) 

    fun hint(target: Int, mode: Int) 

    fun framebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: Int) 

    fun uniform1f(location: Int, x: Float) 

    fun depthMask(flag: Boolean) 

    fun blendColor(red: Float, green: Float, blue: Float, alpha: Float) 

    fun uniformMatrix4fv(location: WebGLUniformLocation, transpose: Boolean, value: FloatArray)

    fun uniformMatrix4fv(location: WebGLUniformLocation, count: Int, transpose: Boolean, value: FloatArray, offset: Int)

    fun bufferData(target: Int, data: FloatArray, usage: Int)

    fun validateProgram(program: Int) 

    fun texParameterf(target: Int, pname: Int, param: Float) 

    fun isFramebuffer(framebuffer: Int): Boolean 

    fun deleteBuffer(buffer: Int) 

    fun shaderSource(shader: WebGLShader, sourcecode: String)

    fun vertexAttrib2fv(indx: Int, values: FloatBuffer?) 

    fun deleteFramebuffers(n: Int, framebuffers: IntArray)

    fun uniform4fv(location: Int, count: Int, v: FloatBuffer?) 

    fun uniform4fv(location: Int, count: Int, v: FloatArray?, offset: Int) 

    fun compressedTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, imageSize: Int, data: Int8Array)

    fun generateMipmap(target: Int) 

    fun deleteProgram(program: Int) 

    fun framebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int) 

    fun genRenderbuffer(): Int 

    fun attachShader(program: WebGLProgram, shader: WebGLShader)

    fun bindBuffer(target: Int, buffer: WebGLBuffer)

    fun shaderBinary(n: Int, shaders: IntArray, binaryformat: Int, binary: Int8Array, length: Int)

    fun disable(cap: Int) 

    fun getRenderbufferParameteriv(target: Int, pname: Int, params: IntArray)

    fun getShaderInfoLog(shader: Int): String 

    fun getActiveUniform(program: Int, index: Int, size: IntArray, type: IntArray): String

    fun isShader(shader: Int): Boolean 

    fun uniform1i(location: Int, x: Int) 

    fun blendEquationSeparate(modeRGB: Int, modeAlpha: Int) 

    fun scissor(x: Int, y: Int, width: Int, height: Int) 

    fun createProgram(): WebGLProgram

    fun uniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer?) 

    fun uniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatArray?, offset: Int) 

    fun getTexParameterfv(target: Int, pname: Int, params: FloatBuffer?) 

    fun vertexAttrib1f(indx: Int, x: Float) 

    fun uniform1fv(location: Int, count: Int, v: FloatBuffer?) 

    fun uniform1fv(location: Int, count: Int, v: FloatArray?, offset: Int)

    fun uniform3iv(location: Int, count: Int, v: IntArray)

    fun uniform3iv(location: Int, count: Int, v: kotlin.IntArray?, offset: Int)

    fun texImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: Int8Array)

    fun vertexAttrib3fv(indx: Int, values: FloatBuffer?) 

    fun blendFunc(sfactor: Int, dfactor: Int) 

    fun isEnabled(cap: Int): Boolean 

    fun getAttribLocation(program: WebGLProgram, name: String): Int

    fun depthRangef(zNear: Float, zFar: Float) 

    fun flush() 

    fun sampleCoverage(value: Float, invert: Boolean) 

    fun copyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int) 

    fun getShaderiv(shader: Int, pname: Int, params: IntArray)

    fun getUniformfv(program: Int, location: Int, params: FloatBuffer?) 

    fun uniform4f(location: Int, x: Float, y: Float, z: Float, w: Float)

    fun depthFunc(func: Int) 

    fun isBuffer(buffer: Int): Boolean 

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

    fun uniform4i(location: Int, x: Int, y: Int, z: Int, w: Int) 

    fun vertexAttrib1fv(indx: Int, values: FloatBuffer?) 

    fun uniform3fv(location: Int, count: Int, v: FloatBuffer?) 

    fun uniform3fv(location: Int, count: Int, v: FloatArray, offset: Int)

    fun vertexAttrib2f(indx: Int, x: Float, y: Float) 

    fun activeTexture(texture: Int) 

    fun cullFace(mode: Int) 

    fun clearStencil(s: Int) 

    fun getFloatv(pname: Int, params: FloatBuffer?) 

    fun drawArrays(mode: Int, first: Int, count: Int) 

    fun bindFramebuffer(target: Int, framebuffer: Int) 

    fun getError(): Int 

    fun bufferSubData(target: Int, offset: Int, size: Int, data: Int8Array)

    fun copyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int) 

    fun isProgram(program: Int): Boolean 

    fun stencilOp(fail: Int, zfail: Int, zpass: Int) 

    fun disableVertexAttribArray(index: Int) 

    fun genBuffers(n: Int, buffers: IntArray)

    fun getAttachedShaders(program: Int, maxcount: Int, count: Int8Array, shaders: IntArray)

    fun genRenderbuffers(n: Int, renderbuffers: IntArray)

    fun renderbufferStorage(target: Int, internalformat: Int, width: Int, height: Int) 

    fun uniform3f(location: Int, x: Float, y: Float, z: Float) 

    fun readPixels(x: Int, y: Int, width: Int, height: Int, format: Int, type: Int, pixels: Int8Array)

    fun stencilMask(mask: Int) 

    fun blendFuncSeparate(srcRGB: Int, dstRGB: Int, srcAlpha: Int, dstAlpha: Int) 

    fun getShaderPrecisionFormat(shadertype: Int, precisiontype: Int, range: IntArray, precision: IntArray)

    fun isTexture(texture: Int): Boolean 

    fun getVertexAttribfv(index: Int, pname: Int, params: FloatBuffer?) 

    fun getVertexAttribPointerv(index: Int, pname: Int, pointer: Int8Array)

    fun createShader(type: Int): WebGLShader

    fun stencilFuncSeparate(face: Int, func: Int, ref: Int, mask: Int) 

    fun getString(name: Int): String 

    fun compressedTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: Int8Array)

    fun uniform2iv(location: Int, count: Int, v: IntBuffer?)

    fun uniform2iv(location: Int, count: Int, v: kotlin.IntArray?, offset: Int)

    fun createBuffer(): WebGLBuffer

    fun enable(cap: Int) 

    fun getUniformiv(program: Int, location: Int, params: IntBuffer?)

    fun getFramebufferAttachmentParameteriv(target: Int, attachment: Int, pname: Int, params: IntBuffer?)

    fun deleteRenderbuffer(renderbuffer: Int) 

    fun genFramebuffers(n: Int, framebuffers: IntBuffer?)

    fun linkProgram(program: WebGLProgram)

    @OpaqueMethod("getShaderParameter")
    fun getShaderParameterBoolean(shader: WebGLShader, pname: Int):Boolean

    @OpaqueMethod("getShaderParameter")
    fun getShaderParameterInt(shader: WebGLShader, pname: Int):Int

}

