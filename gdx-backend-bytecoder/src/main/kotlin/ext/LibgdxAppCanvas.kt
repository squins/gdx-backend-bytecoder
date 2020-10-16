package ext

interface LibgdxAppCanvas  : HtmlCanvasElement {
    fun audio(name:String) : WebAudio

    fun mat4() : WebMat4
}