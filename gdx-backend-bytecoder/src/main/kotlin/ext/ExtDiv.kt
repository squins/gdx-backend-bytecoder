package ext

import de.mirkosertic.bytecoder.api.OpaqueProperty
import de.mirkosertic.bytecoder.api.web.Element

interface ExtDiv : Element {
    @OpaqueProperty
    fun clientWidth(): Int

    @OpaqueProperty
    fun clientHeight(): Int

    @OpaqueProperty
    fun style(style: String)
}