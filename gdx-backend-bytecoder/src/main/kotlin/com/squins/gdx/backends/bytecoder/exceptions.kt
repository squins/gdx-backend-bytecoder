package com.squins.gdx.backends.bytecoder

fun makeAndLogIllegalArgumentException(tag:String, message:String): IllegalArgumentException {
    printErr("[$tag]: $message")

    return IllegalArgumentException(message)
}

fun printErr(errorMsg:String){
    System.err.println(errorMsg)
}
