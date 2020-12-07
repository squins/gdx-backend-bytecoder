package com.squins.gdx.backends.bytecoder

import org.junit.jupiter.api.Test

class BytecoderApplicationTest{
    val listOfExampleClasses = listOf<Class<Any>>()

    @Test
    internal fun testClassNamesSimple() {
        val listOfSimpleNameClasses = listOf<String>()
        for (i in listOfExampleClasses){
            extractSimpleClassName(i.simpleName)
        }
    }

    fun extractSimpleClassName(fullClassName: String?): String? {
        if (null == fullClassName || "" == fullClassName) return ""
        var lastDot = fullClassName.lastIndexOf('.')
        return if (0 > lastDot) fullClassName else fullClassName.substring(++lastDot)
    }
}