package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.utils.IntMap
import com.badlogic.gdx.utils.IntSet
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import de.mirkosertic.bytecoder.api.web.HTMLCanvasElement


class BytecoderInput(libgdxAppCanvas: LibgdxAppCanvas, config: BytecoderApplicationConfiguration) : Input {
    private val MAX_TOUCHES = 20
    private val MAX_KEYCODE = 255
    private var justTouched = false
    private val touchMap = IntMap<Int>(20)
    private val touched = BooleanArray(MAX_TOUCHES)
    private val touchX = IntArray(MAX_TOUCHES)
    private val touchY = IntArray(MAX_TOUCHES)
    private val deltaX = IntArray(MAX_TOUCHES)
    private val deltaY = IntArray(MAX_TOUCHES)
    var pressedButtons = IntSet()
    var pressedKeyCount = 0
    var pressedKeySet = IntSet()
    var pressedKeys = BooleanArray(MAX_KEYCODE + 1)
    var keyJustPressed = false
    var justPressedKeys = BooleanArray(MAX_KEYCODE + 1)
    var justPressedButtons = BooleanArray(5)
    lateinit var processor: InputProcessor
    var currentEventTimeStamp: Long = 0
//    lateinit var canvas: HTMLCanvasElement
    var config: BytecoderApplicationConfiguration = BytecoderApplicationConfiguration()
    var hasFocus = true
//    var accelerometer: GwtAccelerometer? = null
//    var gyroscope: GwtGyroscope? = null
    private val keysToCatch = IntSet()

//    fun BytecoderInput(canvas: HTMLCanvasElement, config: BytecoderApplicationConfiguration) {
//        this.canvas = canvas
//        this.config = config
//        if (config.useAccelerometer) {
//            if (BytecoderApplication.agentInfo().isFirefox()) {
//                setupAccelerometer()
//            } else {
//                GwtPermissions.queryPermission(GwtAccelerometer.PERMISSION, object : GwtPermissionResult() {
//                    fun granted() {
//                        setupAccelerometer()
//                    }
//
//                    fun denied() {}
//                    fun prompt() {
//                        setupAccelerometer()
//                    }
//                })
//            }
//        }
//        hookEvents()
//    }

    override fun getAccelerometerX(): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getAccelerometerY(): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getAccelerometerZ(): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getGyroscopeX(): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getGyroscopeY(): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getGyroscopeZ(): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getMaxPointers(): Int {
        return MAX_TOUCHES
    }

    override fun getX(): Int {
        return touchX[0]
    }

    override fun getX(pointer: Int): Int {
       return touchX[pointer]
    }

    override fun getDeltaX(): Int {
        return deltaX[0]
    }

    override fun getDeltaX(pointer: Int): Int {
        return deltaX[pointer]
    }

    override fun getY(): Int {
        return touchY[0]
    }

    override fun getY(pointer: Int): Int {
        return touchY[pointer]
    }

    override fun getDeltaY(): Int {
        return deltaY[0]
    }

    override fun getDeltaY(pointer: Int): Int {
        return deltaY[pointer]
    }

    override fun isTouched(): Boolean {
        for (pointer in 0 until MAX_TOUCHES) {
            if (touched[pointer]) {
                return true
            }
        }
        return false
    }

    override fun isTouched(pointer: Int): Boolean {
        return touched[pointer]
    }

    override fun justTouched(): Boolean {
        return justTouched
    }

    override fun getPressure(): Float {
        return getPressure(0)
    }

    override fun getPressure(pointer: Int): Float {
        return if (isTouched(pointer)) 1.toFloat() else 0.toFloat()
    }

    override fun isButtonPressed(button: Int): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isButtonJustPressed(button: Int): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isKeyPressed(key: Int): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isKeyJustPressed(key: Int): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getTextInput(listener: Input.TextInputListener?, title: String?, text: String?, hint: String?) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun setOnscreenKeyboardVisible(visible: Boolean) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun vibrate(milliseconds: Int) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun vibrate(pattern: LongArray?, repeat: Int) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun cancelVibrate() {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getAzimuth(): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getPitch(): Float {
        return 0.toFloat()
    }

    override fun getRoll(): Float {
        return 0.toFloat()
    }

    override fun getRotationMatrix(matrix: FloatArray) {
    }

    override fun getCurrentEventTime(): Long {
       return currentEventTimeStamp
    }

    override fun setCatchBackKey(catchBack: Boolean) {
    }

    override fun isCatchBackKey(): Boolean {
       return false
    }

    override fun setCatchMenuKey(catchMenu: Boolean) {
    }

    override fun isCatchMenuKey(): Boolean {
        return false
    }

    override fun setCatchKey(keycode: Int, catchKey: Boolean) {
    }

    override fun isCatchKey(keycode: Int): Boolean {
        return false
    }

    override fun setInputProcessor(processor: InputProcessor) {
        this.processor = processor
    }

    override fun getInputProcessor(): InputProcessor {
        return processor
    }

    override fun isPeripheralAvailable(peripheral: Input.Peripheral?): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getRotation(): Int {
        return 0
    }

    override fun getNativeOrientation(): Input.Orientation {
        return Input.Orientation.Landscape
    }

    override fun setCursorCatched(catched: Boolean) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isCursorCatched(): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun setCursorPosition(x: Int, y: Int) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

}
