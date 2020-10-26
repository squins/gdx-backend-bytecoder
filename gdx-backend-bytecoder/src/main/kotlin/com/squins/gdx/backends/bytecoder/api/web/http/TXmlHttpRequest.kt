package com.squins.gdx.backends.bytecoder.api.web.http

import de.mirkosertic.bytecoder.api.web.EventTarget
import de.mirkosertic.bytecoder.classlib.java.lang.TObject

abstract class TXmlHttpRequest : TObject(), EventTarget {

    abstract fun open(method:String, url:String)

    abstract fun send()

    companion object {
        external fun create(): TXmlHttpRequest
    }
}