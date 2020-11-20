package com.squins.gdx.backends.bytecoder

class BytecoderApplicationConfiguration

@JvmOverloads constructor(
        val width: Int,
        /** the height of the drawing area in pixels, or 0 for using the available space  */
        val height: Int,
        /** Whether to use physical device pixels or CSS pixels for scaling the canvas. Makes a difference on mobile
         * devices and HDPI and Retina displays. Set to true for resizable and fullscreen games on mobile devices and for
         * Desktops if you want to use the full resolution of HDPI/Retina displays.<br></br>
         * Setting to false mostly makes sense for
         * fixed-size games or non-mobile games expecting performance issues on huge resolutions. If you target mobiles
         * and desktops, consider using physical device pixels on mobile devices only by using the return value of
         * [GwtApplication.isMobileDevice] .  */
        val usePhysicalPixels: Boolean = false) {
    /** If true, audio backend will not be used. This means [Application.getAudio] returns null.  */
    var disableAudio = false

    /**
     * Padding to use for resizing the game content in the browser window, for resizable applications only.
     * Defaults to 10. The padding is necessary to prevent the browser from showing scrollbars. This
     * can happen if the game content is of the same size than the browser window.
     * The padding is given in logical pixels, not affected by [.usePhysicalPixels].
     */
    var padHorizontal = 10
    var padVertical = 10

    /** whether to use a stencil buffer  */
    var stencil = false

    /** whether to enable antialiasing  */
    var antialiasing = false

    /** the id of a canvas element to be used as the drawing area, can be null in which case a Panel and Canvas are added to the
     * body element of the DOM  */
    var canvasId: String? = null

    /** whether to use debugging mode for OpenGL calls. Errors will result in a RuntimeException being thrown.  */
    var useDebugGL = false

    /** preserve the back buffer, needed if you fetch a screenshot via canvas#toDataUrl, may have performance impact  */
    var preserveDrawingBuffer = false

    /** whether to include an alpha channel in the color buffer to combine the color buffer with the rest of the webpage
     * effectively allows transparent backgrounds in GWT, at a performance cost.  */
    var alpha = false

    /** whether to use premultipliedalpha, may have performance impact   */
    var premultipliedAlpha = false

    /** screen-orientation to attempt locking as the application enters full-screen-mode. Note that on mobile browsers, full-screen
     * mode can typically only be entered on a user gesture (click, tap, key-stroke)  */
    var fullscreenOrientation: BytecoderGraphics.OrientationLockType? = null

    /* Whether openURI will open page in new tab. By default it will, however if may be blocked by popup blocker. */ /* To prevent the page from being blocked you can redirect to the new page. However this will exit your game.  */
    var openURLInNewWindow = true

    /** whether to use the accelerometer. default: true  */
    var useAccelerometer = true

    /** whether to use the gyroscope. default: false  */
    var useGyroscope = false

    @JvmOverloads
    constructor(usePhysicalPixels: Boolean = false) : this(0, 0, usePhysicalPixels) {
    }

    val isFixedSizeApplication: Boolean
        get() = width != 0 && height != 0
}