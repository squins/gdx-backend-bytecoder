package ext

import com.badlogic.gdx.files.FileHandle
import de.mirkosertic.bytecoder.api.OpaqueReferenceType

interface WebFiles : OpaqueReferenceType {
    fun internal() : FileHandle
}