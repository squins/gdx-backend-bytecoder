package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas

class BytecoderInput(libgdxAppCanvas: LibgdxAppCanvas, config: BytecoderApplicationConfiguration) : Input {
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
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getX(): Int {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getX(pointer: Int): Int {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getDeltaX(): Int {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getDeltaX(pointer: Int): Int {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getY(): Int {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getY(pointer: Int): Int {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getDeltaY(): Int {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getDeltaY(pointer: Int): Int {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isTouched(): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isTouched(pointer: Int): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun justTouched(): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getPressure(): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getPressure(pointer: Int): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
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
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getRoll(): Float {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getRotationMatrix(matrix: FloatArray?) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getCurrentEventTime(): Long {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun setCatchBackKey(catchBack: Boolean) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isCatchBackKey(): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun setCatchMenuKey(catchMenu: Boolean) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isCatchMenuKey(): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun setCatchKey(keycode: Int, catchKey: Boolean) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isCatchKey(keycode: Int): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun setInputProcessor(processor: InputProcessor?) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getInputProcessor(): InputProcessor {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun isPeripheralAvailable(peripheral: Input.Peripheral?): Boolean {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getRotation(): Int {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    override fun getNativeOrientation(): Input.Orientation {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
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
