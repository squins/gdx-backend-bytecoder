package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.utils.IntMap
import com.badlogic.gdx.utils.IntSet
import com.badlogic.gdx.utils.TimeUtils
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import de.mirkosertic.bytecoder.api.web.Event
import de.mirkosertic.bytecoder.api.web.EventListener
import de.mirkosertic.bytecoder.api.web.MouseEvent
import de.mirkosertic.bytecoder.api.web.Node
import kotlin.math.roundToInt

/**
 * Code based on GWT Implementation:
 * https://github.com/libgdx/libgdx/blob/master/backends/gdx-backends-gwt/src/com/badlogic/gdx/backends/gwt/DefaultGwtInput.java
 */

private const val MAX_TOUCHES = 20

class BytecoderInput(val canvas: LibgdxAppCanvas, config: BytecoderApplicationConfiguration) : Input, EventListener<Event> {

    var justTouched = false
    private val touchMap = IntMap<Int>(MAX_TOUCHES)
    private val touched = BooleanArray(MAX_TOUCHES)
    private val touchX = IntArray(MAX_TOUCHES)
    private val touchY = IntArray(MAX_TOUCHES)
    private val deltaX = IntArray(MAX_TOUCHES)
    private val deltaY = IntArray(MAX_TOUCHES)

    var pressedButtons = IntSet()
    var justPressedButtons = BooleanArray(5);
    var hasFocus = false
    var processor: InputProcessor? = null
    var currentEventTimeStamp: Long = 0

    init {
        hookEvents();
    }

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
        return justTouched
    }

    override fun getPressure(): Float {
        return getPressure(0)
    }

    override fun getPressure(pointer: Int): Float {
        return if (isTouched(pointer)) 1f else 0f
    }

    override fun isButtonPressed(button: Int): Boolean {
        return pressedButtons.contains(button) && touched[0]
    }

    override fun isButtonJustPressed(button: Int): Boolean {
        if (button < 0 || button >= justPressedButtons.size) return false;
        return justPressedButtons[button];
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
        return currentEventTimeStamp
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
        this.processor = processor
    }

    override fun getInputProcessor(): InputProcessor? {
        return processor
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
        return false
    }

    override fun setCursorPosition(x: Int, y: Int) {
        throw makeAndLogIllegalArgumentException("BytecoderInput", "Not yet implemented")
    }

    private fun hookEvents() {
        addEventListener(canvas, "mousedown", this);
        addEventListener(canvas, "mouseup", this);
        addEventListener(canvas, "mousemove", this);
        /*addEventListener(Document.get(), "mousedown", this);
        addEventListener(Document.get(), "mouseup", this);
        addEventListener(Document.get(), "mousemove", this);
        addEventListener(canvas, getMouseWheelEvent(), this);
        addEventListener(Document.get(), "keydown", this);
        addEventListener(Document.get(), "keyup", this);
        addEventListener(Document.get(), "keypress", this);
        addEventListener(getWindow(), "blur", this);

        addEventListener(canvas, "touchstart", this);
        addEventListener(canvas, "touchmove", this);
        addEventListener(canvas, "touchcancel", this);
        addEventListener(canvas, "touchend", this);*/
    }

    private fun addEventListener(target: Node, name: String, handler: EventListener<Event>) {
        target.addEventListener(name, handler);
    }

    private fun getButton(button: Int): Int {
        if (button == 0) return Buttons.LEFT
        if (button == 2) return Buttons.RIGHT
        return if (button == 1) Buttons.MIDDLE else Buttons.LEFT
    }

    override fun run(e: Event) {
        if (e.type().equals("mousedown")) {
            val event: MouseEvent = e as MouseEvent
            val button: Int = getButton(event.button())
            if (pressedButtons.contains(button)) {
                val mouseX = event.clientX()
                val mouseY = event.clientY()
                if (mouseX < 0 || mouseX > Gdx.graphics.width || mouseY < 0 || mouseY > Gdx.graphics.height) {
                    hasFocus = false
                }
                return
            }
            hasFocus = true
            this.justTouched = true;
            this.touched[0] = true;
            this.pressedButtons.add(button)
            justPressedButtons[button] = true
            this.deltaX[0] = 0
            this.deltaY[0] = 0

            if (!isCursorCatched) {
                this.touchX[0] = getRelativeX(e, canvas)
                this.touchY[0] = getRelativeY(e, canvas)
            }

            this.currentEventTimeStamp = TimeUtils.nanoTime()
            processor?.touchDown(touchX[0], touchY[0], 0, button)
        }

        if (e.type().equals("mousemove")) {
            val event: MouseEvent = e as MouseEvent
            if (!isCursorCatched) {
                this.deltaX[0] = getRelativeX(event, canvas) - touchX[0];
                this.deltaY[0] = getRelativeY(event, canvas) - touchY[0];
                this.touchX[0] = getRelativeX(event, canvas);
                this.touchY[0] = getRelativeY(event, canvas);
            }
            this.currentEventTimeStamp = TimeUtils.nanoTime();

            if (touched[0])
                processor?.touchDragged(touchX[0], touchY[0], 0);
            else
                processor?.mouseMoved(touchX[0], touchY[0]);
        }

        if (e.type().equals("mouseup")) {
            val event: MouseEvent = e as MouseEvent
            val button: Int = getButton(event.button())
            if (!pressedButtons.contains(button)) return;
            this.pressedButtons.remove(button);
            this.touched[0] = pressedButtons.size > 0;

            if (!isCursorCatched) {
                this.deltaX[0] = getRelativeX(event, canvas) - touchX[0]
                this.deltaY[0] = getRelativeY(event, canvas) - touchY[0]
                this.touchX[0] = getRelativeX(event, canvas)
                this.touchY[0] = getRelativeY(event, canvas)
            }

            this.currentEventTimeStamp = TimeUtils.nanoTime();
            this.touched[0] = false;
            processor?.touchUp(touchX[0], touchY[0], 0, button)
        }
    }

    private fun getRelativeX(e: MouseEvent, canvas: LibgdxAppCanvas): Int {
        return e.clientX().roundToInt();
    }

    private fun getRelativeY(e: MouseEvent, canvas: LibgdxAppCanvas): Int {
        return e.clientY().roundToInt();
    }

}
