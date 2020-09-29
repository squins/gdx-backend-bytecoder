package ext

interface LibgdxAppCanvas  : HtmlCanvasElement {
    fun audio(name:String) : WebAudio
}