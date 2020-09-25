package ext

import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.web.Window

abstract class ExtWindow : Window() {
    @get:OpaqueProperty
    abstract val devicePixelRatio: Float
}