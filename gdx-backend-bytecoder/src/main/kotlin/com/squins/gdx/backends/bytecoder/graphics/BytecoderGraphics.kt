package com.squins.gdx.backends.bytecoder.graphics

import com.badlogic.gdx.Graphics
import com.badlogic.gdx.graphics.Cursor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.glutils.GLVersion
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import de.mirkosertic.bytecoder.api.web.webgl.WebGLRenderingContext

class BytecoderGraphics(private val libgdxAppCanvas: LibgdxAppCanvas) : Graphics {
    private val gl : WebGLRenderingContext = libgdxAppCanvas.getContext("webgl")
    private val bytecoderGL20 = BytecoderGL20(gl)
    private var fps : Float = 0f
    private var lastTimeStamp : Long = System.currentTimeMillis()
    private var time : Float = 0f
    private var frames : Int = 0
    var frameId = -1
    var delta = 0f

    /* Enum values from http://www.w3.org/TR/screen-orientation. Filtered based on what the browsers actually support. */
    enum class OrientationLockType(val nameOrientation: String) {
        LANDSCAPE("landscape"), PORTRAIT("portrait"), PORTRAIT_PRIMARY("portrait-primary"), PORTRAIT_SECONDARY(
                "portrait-secondary"),
        LANDSCAPE_PRIMARY("landscape-primary"), LANDSCAPE_SECONDARY("landscape-secondary");

    }

    override fun isGL30Available(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getGL20(): GL20 {
        return bytecoderGL20
    }

    override fun getGL30(): GL30 {
        TODO("Not yet implemented")
    }

    override fun setGL20(gl20: GL20?) {
        TODO("Not yet implemented")
    }

    override fun setGL30(gl30: GL30?) {
        TODO("Not yet implemented")
    }

    override fun getWidth(): Int {
        return libgdxAppCanvas.width()
    }

    override fun getHeight(): Int {
        return libgdxAppCanvas.height()
    }

    override fun getBackBufferWidth(): Int {
        TODO("Not yet implemented")
    }

    override fun getBackBufferHeight(): Int {
        TODO("Not yet implemented")
    }

    override fun getSafeInsetLeft(): Int {
        TODO("Not yet implemented")
    }

    override fun getSafeInsetTop(): Int {
        TODO("Not yet implemented")
    }

    override fun getSafeInsetBottom(): Int {
        TODO("Not yet implemented")
    }

    override fun getSafeInsetRight(): Int {
        TODO("Not yet implemented")
    }

    override fun getFrameId(): Long {
        return frameId.toLong()
    }

    override fun getDeltaTime(): Float {
        return delta
    }

    override fun getRawDeltaTime(): Float {
        TODO("Not yet implemented")
    }

    override fun getFramesPerSecond(): Int {
        return fps.toInt()
    }

    override fun getType(): Graphics.GraphicsType {
        return Graphics.GraphicsType.WebGL
    }

    override fun getGLVersion(): GLVersion {
        TODO("Not yet implemented")
    }

    override fun getPpiX(): Float {
        TODO("Not yet implemented")
    }

    override fun getPpiY(): Float {
        TODO("Not yet implemented")
    }

    override fun getPpcX(): Float {
        TODO("Not yet implemented")
    }

    override fun getPpcY(): Float {
        TODO("Not yet implemented")
    }

    override fun getDensity(): Float {
        TODO("Not yet implemented")
    }

    override fun supportsDisplayModeChange(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPrimaryMonitor(): Graphics.Monitor {
        TODO("Not yet implemented")
    }

    override fun getMonitor(): Graphics.Monitor {
        TODO("Not yet implemented")
    }

    override fun getMonitors(): Array<Graphics.Monitor> {
        TODO("Not yet implemented")
    }

    override fun getDisplayModes(): Array<Graphics.DisplayMode> {
        TODO("Not yet implemented")
    }

    override fun getDisplayModes(monitor: Graphics.Monitor?): Array<Graphics.DisplayMode> {
        TODO("Not yet implemented")
    }

    override fun getDisplayMode(): Graphics.DisplayMode {
        TODO("Not yet implemented")
    }

    override fun getDisplayMode(monitor: Graphics.Monitor?): Graphics.DisplayMode {
        TODO("Not yet implemented")
    }

    override fun setFullscreenMode(displayMode: Graphics.DisplayMode?): Boolean {
        TODO("Not yet implemented")
    }

    override fun setWindowedMode(width: Int, height: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun setTitle(title: String?) {
        TODO("Not yet implemented")
    }

    override fun setUndecorated(undecorated: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setResizable(resizable: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setVSync(vsync: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getBufferFormat(): Graphics.BufferFormat {
        TODO("Not yet implemented")
    }

    override fun supportsExtension(extensionName: String): Boolean {
//        println("supportsExtension $extensionName returning always true")
        return false
    }

    fun update () {
        val currTimeStamp = System.currentTimeMillis()
        val deltaTime = (currTimeStamp - lastTimeStamp) / 1000.0f
        lastTimeStamp = currTimeStamp
        time += deltaTime
        frames++
        if (time > 1) {
            fps = frames.toFloat()
            time = 0f
            frames = 0
        }
    }

    override fun setContinuousRendering(isContinuous: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isContinuousRendering(): Boolean {
        TODO("Not yet implemented")
    }

    override fun requestRendering() {
        TODO("Not yet implemented")
    }

    override fun isFullscreen(): Boolean {
        TODO("Not yet implemented")
    }

    override fun newCursor(pixmap: Pixmap?, xHotspot: Int, yHotspot: Int): Cursor {
        TODO("Not yet implemented")
    }

    override fun setCursor(cursor: Cursor?) {
        TODO("Not yet implemented")
    }

    override fun setSystemCursor(systemCursor: Cursor.SystemCursor?) {
        TODO("Not yet implemented")
    }
}