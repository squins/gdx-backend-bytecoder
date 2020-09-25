package ext

import de.mirkosertic.bytecoder.api.web.CanvasRenderingContext2D
import de.mirkosertic.bytecoder.api.web.HTMLElement

interface HtmlCanvasElement : HTMLElement {
    fun getContext(context: String?) : WebGLRenderingContext
}