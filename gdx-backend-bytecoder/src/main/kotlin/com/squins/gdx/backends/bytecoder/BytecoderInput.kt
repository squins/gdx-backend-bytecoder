package com.squins.gdx.backends.bytecoder

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.utils.IntMap
import com.badlogic.gdx.utils.IntSet
import com.badlogic.gdx.utils.IntSet.IntSetIterator
import com.badlogic.gdx.utils.TimeUtils
import com.squins.gdx.backends.bytecoder.api.web.LibgdxAppCanvas
import de.mirkosertic.bytecoder.api.web.*
import kotlin.math.roundToInt


/**
 * Code based on GWT Implementation:
 * https://github.com/libgdx/libgdx/blob/master/backends/gdx-backends-gwt/src/com/badlogic/gdx/backends/gwt/DefaultGwtInput.java
 */

class BytecoderInput (val canvas: LibgdxAppCanvas,
                     config: BytecoderApplicationConfiguration) : Input, EventListener<Event> {

    private val MAX_BUTTONS = 5
    private val MAX_TOUCHES = 20

    var justTouched = false
    private val touchMap = IntMap<Int>(MAX_TOUCHES)
    private val touched = BooleanArray(MAX_TOUCHES)
    private val touchX = IntArray(MAX_TOUCHES)
    private val touchY = IntArray(MAX_TOUCHES)
    private val deltaX = IntArray(MAX_TOUCHES)
    private val deltaY = IntArray(MAX_TOUCHES)

    var pressedButtons = IntSet()
    var pressedKeySet = IntSet()

    var justPressedButtons = BooleanArray(MAX_BUTTONS);
    var hasFocus = false
    var processor: InputProcessor? = null
    var currentEventTimeStamp: Long = 0

    // From AbstractInput (Introduced after libGDX 1.9.11)
    val MAX_KEYCODE: Int = 255
    protected val pressedKeys: BooleanArray = BooleanArray(MAX_KEYCODE + 1)
    protected val justPressedKeys: BooleanArray = BooleanArray(MAX_KEYCODE + 1)
    private val keysToCatch = IntSet()
    protected var pressedKeyCount = 0
    protected var keyJustPressed = false

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
        return MAX_TOUCHES
    }

    override fun getX(): Int {
        return getX(0)
    }

    override fun getX(pointer: Int): Int {
        return touchX[pointer]
    }

    override fun getDeltaX(): Int {
        return getDeltaX(0)
    }

    override fun getDeltaX(pointer: Int): Int {
        return deltaX[pointer]
    }

    override fun getY(): Int {
        return getY(0)
    }

    override fun getY(pointer: Int): Int {
        return touchY[pointer]
    }

    override fun getDeltaY(): Int {
        return getDeltaY(0)
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
        return 0f
    }

    override fun getPitch(): Float {
        return 0f
    }

    override fun getRoll(): Float {
        return 0f
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
        addEventListener(canvas, "keydown", this);
        addEventListener(canvas, "keyup", this);
        addEventListener(canvas, "keypress", this);
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
        if (button == 1) {
            return Buttons.MIDDLE
        }
        if (button == 2) {
            return Buttons.RIGHT
        }
        return Buttons.LEFT
    }

    override fun run(e: Event) {
        if (e.type().equals("mousedown")) {
            val event: MouseEvent = e as MouseEvent
            val button: Int = getButton(event.button())
            if (pressedButtons.contains(button)) {
                val mouseX = event.clientX()
                val mouseY = event.clientY()
                if (outsideScreen(mouseX, mouseY)) {
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
        if (hasFocus && !e.type().equals("blur")) {
            if (e.type().equals("keydown")) {
                val event: KeyboardEvent = e as KeyboardEvent
                val code: Int = keyForCode(event.keyCode(), event.location())
                if (isCatchKey(code)) {
                    e.preventDefault()
                }
                if (code == Input.Keys.BACKSPACE) {
                    if (processor != null) {
                        processor!!.keyDown(code)
                        processor!!.keyTyped('\b')
                    }
                } else {
                    if (!pressedKeys!![code]) {
                        pressedKeySet.add(code)
                        pressedKeyCount++
                        pressedKeys!![code] = true
                        keyJustPressed = true
                        justPressedKeys[code] = true
                        if (processor != null) {
                            processor!!.keyDown(code)
                        }
                    }
                }
            }
            if (e.type().equals("keypress")) {
                val event: KeyboardEvent = e as KeyboardEvent
                val c = event.charCode() as Char
                // usually, browsers don't send a keypress event for tab, so we emulate it in
                // keyup event. Just in case this changes in the future, we sort this out here
                // to avoid sending the event twice.
                if (c != '\t') {
                    if (processor != null) processor!!.keyTyped(c)
                }
            }
            if (e.type().equals("keyup")) {
                val event: KeyboardEvent = e as KeyboardEvent
                val code: Int = keyForCode(event.keyCode(), event.location())
                if (isCatchKey(code)) {
                    e.preventDefault()
                }
                if (processor != null && code == Input.Keys.TAB) {
                    // js does not raise keypress event for tab, so emulate this here for
                    // platform-independant behaviour
                    processor!!.keyTyped('\t')
                }
                if (pressedKeys[code]) {
                    pressedKeySet.remove(code)
                    pressedKeyCount--
                    pressedKeys[code] = false
                }
                if (processor != null) {
                    processor!!.keyUp(code)
                }
            }
        } else if (pressedKeyCount > 0) {
            // Gdx.app.log("DefaultGwtInput", "unfocused");
            val iterator: IntSetIterator = pressedKeySet.iterator()
            while (iterator.hasNext) {
                val code = iterator.next()
                if (pressedKeys[code]) {
                    pressedKeySet.remove(code)
                    pressedKeyCount--
                    pressedKeys[code] = false
                }
                if (processor != null) {
                    processor!!.keyUp(code)
                }
            }
        }
    }

    private fun outsideScreen(mouseX: Float, mouseY: Float): Boolean {
        return mouseX < 0 || mouseX > Gdx.graphics.width || mouseY < 0 || mouseY > Gdx.graphics.height;
    }

    private fun getRelativeX(e: MouseEvent, canvas: LibgdxAppCanvas): Int {
        return e.clientX().roundToInt();
    }

    private fun getRelativeY(e: MouseEvent, canvas: LibgdxAppCanvas): Int {
        return e.clientY().roundToInt();
    }

    /** borrowed from PlayN, thanks guys  */
    protected fun keyForCode(keyCode: Int, location: Int): Int {
        return when (keyCode) {
            //KeyCodes.KEY_ALT -> if (location == LOCATION_RIGHT) Input.Keys.ALT_RIGHT else Input.Keys.ALT_LEFT
            //KeyCodes.KEY_BACKSPACE -> Input.Keys.BACKSPACE
            //KeyCodes.KEY_CTRL -> if (location == LOCATION_RIGHT) Input.Keys.CONTROL_RIGHT else Input.Keys.CONTROL_LEFT
            //KeyCodes.KEY_DELETE -> Input.Keys.FORWARD_DEL
            //KeyCodes.KEY_DOWN -> Input.Keys.DOWN
            //KeyCodes.KEY_END -> Input.Keys.END
            //KeyCodes.KEY_ENTER -> if (location == LOCATION_NUMPAD) Input.Keys.NUMPAD_ENTER else Input.Keys.ENTER
            //KeyCodes.KEY_ESCAPE -> Input.Keys.ESCAPE
            //KeyCodes.KEY_HOME -> Input.Keys.HOME
            //KeyCodes.KEY_LEFT -> Input.Keys.LEFT
            //KeyCodes.KEY_PAGEDOWN -> Input.Keys.PAGE_DOWN
            //KeyCodes.KEY_PAGEUP -> Input.Keys.PAGE_UP
            //KeyCodes.KEY_RIGHT -> Input.Keys.RIGHT
            //KeyCodes.KEY_SHIFT -> if (location == LOCATION_RIGHT) Input.Keys.SHIFT_RIGHT else Input.Keys.SHIFT_LEFT
            //KeyCodes.KEY_TAB -> Input.Keys.TAB
            //KeyCodes.KEY_UP -> Input.Keys.UP
            //KEY_PAUSE -> Input.Keys.PAUSE
            //KEY_CAPS_LOCK -> Input.Keys.CAPS_LOCK
            KEY_SPACE -> Input.Keys.SPACE
            KEY_INSERT -> Input.Keys.INSERT
            KEY_0 -> Input.Keys.NUM_0
            KEY_1 -> Input.Keys.NUM_1
            KEY_2 -> Input.Keys.NUM_2
            KEY_3 -> Input.Keys.NUM_3
            KEY_4 -> Input.Keys.NUM_4
            KEY_5 -> Input.Keys.NUM_5
            KEY_6 -> Input.Keys.NUM_6
            KEY_7 -> Input.Keys.NUM_7
            KEY_8 -> Input.Keys.NUM_8
            KEY_9 -> Input.Keys.NUM_9
            KEY_A -> Input.Keys.A
            KEY_B -> Input.Keys.B
            KEY_C -> Input.Keys.C
            KEY_D -> Input.Keys.D
            KEY_E -> Input.Keys.E
            KEY_F -> Input.Keys.F
            KEY_G -> Input.Keys.G
            KEY_H -> Input.Keys.H
            KEY_I -> Input.Keys.I
            KEY_J -> Input.Keys.J
            KEY_K -> Input.Keys.K
            KEY_L -> Input.Keys.L
            KEY_M -> Input.Keys.M
            KEY_N -> Input.Keys.N
            KEY_O -> Input.Keys.O
            KEY_P -> Input.Keys.P
            KEY_Q -> Input.Keys.Q
            KEY_R -> Input.Keys.R
            KEY_S -> Input.Keys.S
            KEY_T -> Input.Keys.T
            KEY_U -> Input.Keys.U
            KEY_V -> Input.Keys.V
            KEY_W -> Input.Keys.W
            KEY_X -> Input.Keys.X
            KEY_Y -> Input.Keys.Y
            KEY_Z -> Input.Keys.Z
            KEY_LEFT_WINDOW_KEY -> Input.Keys.UNKNOWN // FIXME
            KEY_RIGHT_WINDOW_KEY -> Input.Keys.UNKNOWN // FIXME
            KEY_NUMPAD0 -> Input.Keys.NUMPAD_0
            KEY_NUMPAD1 -> Input.Keys.NUMPAD_1
            KEY_NUMPAD2 -> Input.Keys.NUMPAD_2
            KEY_NUMPAD3 -> Input.Keys.NUMPAD_3
            KEY_NUMPAD4 -> Input.Keys.NUMPAD_4
            KEY_NUMPAD5 -> Input.Keys.NUMPAD_5
            KEY_NUMPAD6 -> Input.Keys.NUMPAD_6
            KEY_NUMPAD7 -> Input.Keys.NUMPAD_7
            KEY_NUMPAD8 -> Input.Keys.NUMPAD_8
            KEY_NUMPAD9 -> Input.Keys.NUMPAD_9
            //KEY_MULTIPLY -> Input.Keys.NUMPAD_MULTIPLY
            //KEY_ADD -> Input.Keys.NUMPAD_ADD
            //KEY_SUBTRACT -> Input.Keys.NUMPAD_SUBTRACT
            //KEY_DECIMAL_POINT_KEY -> Input.Keys.NUMPAD_DOT
            //KEY_DIVIDE -> Input.Keys.NUMPAD_DIVIDE
            KEY_F1 -> Input.Keys.F1
            KEY_F2 -> Input.Keys.F2
            KEY_F3 -> Input.Keys.F3
            KEY_F4 -> Input.Keys.F4
            KEY_F5 -> Input.Keys.F5
            KEY_F6 -> Input.Keys.F6
            KEY_F7 -> Input.Keys.F7
            KEY_F8 -> Input.Keys.F8
            KEY_F9 -> Input.Keys.F9
            KEY_F10 -> Input.Keys.F10
            KEY_F11 -> Input.Keys.F11
            KEY_F12 -> Input.Keys.F12
            //KEY_F13 -> Input.Keys.F13
            //KEY_F14 -> Input.Keys.F14
            //KEY_F15 -> Input.Keys.F15
            //KEY_F16 -> Input.Keys.F16
            //KEY_F17 -> Input.Keys.F17
            //KEY_F18 -> Input.Keys.F18
            //KEY_F19 -> Input.Keys.F19
            //KEY_F20 -> Input.Keys.F20
            //KEY_F21 -> Input.Keys.F21
            //KEY_F22 -> Input.Keys.F22
            //KEY_F23 -> Input.Keys.F23
            //KEY_F24 -> Input.Keys.F24
            //KEY_NUM_LOCK -> Input.Keys.NUM_LOCK
            //KEY_SCROLL_LOCK -> Input.Keys.SCROLL_LOCK
            KEY_AUDIO_VOLUME_DOWN, KEY_AUDIO_VOLUME_DOWN_FIREFOX -> Input.Keys.VOLUME_DOWN
            KEY_AUDIO_VOLUME_UP, KEY_AUDIO_VOLUME_UP_FIREFOX -> Input.Keys.VOLUME_UP
            KEY_MEDIA_TRACK_NEXT -> Input.Keys.MEDIA_NEXT
            KEY_MEDIA_TRACK_PREVIOUS -> Input.Keys.MEDIA_PREVIOUS
            KEY_MEDIA_STOP -> Input.Keys.MEDIA_STOP
            KEY_MEDIA_PLAY_PAUSE -> Input.Keys.MEDIA_PLAY_PAUSE
            //KeyCodes.KEY_PRINT_SCREEN -> Input.Keys.PRINT_SCREEN
            KEY_SEMICOLON -> Input.Keys.SEMICOLON
            KEY_EQUALS -> Input.Keys.EQUALS
            KEY_COMMA -> Input.Keys.COMMA
            KEY_DASH -> Input.Keys.MINUS
            KEY_PERIOD -> Input.Keys.PERIOD
            KEY_FORWARD_SLASH -> Input.Keys.SLASH
            KEY_GRAVE_ACCENT -> Input.Keys.UNKNOWN // FIXME
            KEY_OPEN_BRACKET -> Input.Keys.LEFT_BRACKET
            KEY_BACKSLASH -> Input.Keys.BACKSLASH
            KEY_CLOSE_BRACKET -> Input.Keys.RIGHT_BRACKET
            KEY_SINGLE_QUOTE -> Input.Keys.APOSTROPHE
            else -> Input.Keys.UNKNOWN
        }
    }

    // these are absent from KeyCodes; we know not why...
    private val KEY_PAUSE = 19
    private val KEY_CAPS_LOCK = 20
    private val KEY_SPACE = 32
    private val KEY_INSERT = 45
    private val KEY_0 = 48
    private val KEY_1 = 49
    private val KEY_2 = 50
    private val KEY_3 = 51
    private val KEY_4 = 52
    private val KEY_5 = 53
    private val KEY_6 = 54
    private val KEY_7 = 55
    private val KEY_8 = 56
    private val KEY_9 = 57
    private val KEY_A = 65
    private val KEY_B = 66
    private val KEY_C = 67
    private val KEY_D = 68
    private val KEY_E = 69
    private val KEY_F = 70
    private val KEY_G = 71
    private val KEY_H = 72
    private val KEY_I = 73
    private val KEY_J = 74
    private val KEY_K = 75
    private val KEY_L = 76
    private val KEY_M = 77
    private val KEY_N = 78
    private val KEY_O = 79
    private val KEY_P = 80
    private val KEY_Q = 81
    private val KEY_R = 82
    private val KEY_S = 83
    private val KEY_T = 84
    private val KEY_U = 85
    private val KEY_V = 86
    private val KEY_W = 87
    private val KEY_X = 88
    private val KEY_Y = 89
    private val KEY_Z = 90
    private val KEY_LEFT_WINDOW_KEY = 91
    private val KEY_RIGHT_WINDOW_KEY = 92
    private val KEY_SELECT_KEY = 93
    private val KEY_NUMPAD0 = 96
    private val KEY_NUMPAD1 = 97
    private val KEY_NUMPAD2 = 98
    private val KEY_NUMPAD3 = 99
    private val KEY_NUMPAD4 = 100
    private val KEY_NUMPAD5 = 101
    private val KEY_NUMPAD6 = 102
    private val KEY_NUMPAD7 = 103
    private val KEY_NUMPAD8 = 104
    private val KEY_NUMPAD9 = 105
    private val KEY_MULTIPLY = 106
    private val KEY_ADD = 107
    private val KEY_SUBTRACT = 109
    private val KEY_DECIMAL_POINT_KEY = 110
    private val KEY_DIVIDE = 111
    private val KEY_F1 = 112
    private val KEY_F2 = 113
    private val KEY_F3 = 114
    private val KEY_F4 = 115
    private val KEY_F5 = 116
    private val KEY_F6 = 117
    private val KEY_F7 = 118
    private val KEY_F8 = 119
    private val KEY_F9 = 120
    private val KEY_F10 = 121
    private val KEY_F11 = 122
    private val KEY_F12 = 123
    private val KEY_F13 = 124
    private val KEY_F14 = 125
    private val KEY_F15 = 126
    private val KEY_F16 = 127
    private val KEY_F17 = 128
    private val KEY_F18 = 129
    private val KEY_F19 = 130
    private val KEY_F20 = 131
    private val KEY_F21 = 132
    private val KEY_F22 = 133
    private val KEY_F23 = 134
    private val KEY_F24 = 135
    private val KEY_NUM_LOCK = 144
    private val KEY_SCROLL_LOCK = 145
    private val KEY_AUDIO_VOLUME_DOWN = 174
    private val KEY_AUDIO_VOLUME_UP = 175
    private val KEY_MEDIA_TRACK_NEXT = 176
    private val KEY_MEDIA_TRACK_PREVIOUS = 177
    private val KEY_MEDIA_STOP = 178
    private val KEY_MEDIA_PLAY_PAUSE = 179
    private val KEY_AUDIO_VOLUME_DOWN_FIREFOX = 182
    private val KEY_AUDIO_VOLUME_UP_FIREFOX = 183
    private val KEY_SEMICOLON = 186
    private val KEY_EQUALS = 187
    private val KEY_COMMA = 188
    private val KEY_DASH = 189
    private val KEY_PERIOD = 190
    private val KEY_FORWARD_SLASH = 191
    private val KEY_GRAVE_ACCENT = 192
    private val KEY_OPEN_BRACKET = 219
    private val KEY_BACKSLASH = 220
    private val KEY_CLOSE_BRACKET = 221
    private val KEY_SINGLE_QUOTE = 222

    private val LOCATION_STANDARD = 0
    private val LOCATION_LEFT = 1
    private val LOCATION_RIGHT = 2
    private val LOCATION_NUMPAD = 3
}
