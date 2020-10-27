package com.squins.gdx.backends.bytecoder.api.web.http

import de.mirkosertic.bytecoder.api.Callback
import de.mirkosertic.bytecoder.api.web.EventTarget
import de.mirkosertic.bytecoder.classlib.java.lang.TObject

abstract class TXmlHttpRequest : TObject(), EventTarget {

    lateinit var onReadyStateChange: Callback

    abstract fun open(method:String, url:String)

    abstract fun send()

    abstract fun setRequestHeader(header: String, value: String)

    abstract fun getReadyState()

    abstract fun getStatus() : Int

    abstract fun getResponseText() : String

    companion object {
        external fun create(): TXmlHttpRequest
    }
}