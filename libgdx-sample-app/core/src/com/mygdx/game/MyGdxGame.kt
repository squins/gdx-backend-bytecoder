package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram


class MyGdxGame : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var img: Texture
    lateinit var sampleMusic: Music

    override fun create() {
        println("Create")
        val fileReference = Gdx.files.internal("sample.mp3")
        println("Created fileReference")

//        sampleMusic = Gdx.audio.newMusic(fileReference)
//        println("Music connected to file, now play")
//
//        sampleMusic.setOnCompletionListener {  }
//
//        sampleMusic.isLooping = false
//        println("looping is false")
//
//        sampleMusic.volume = 0.5f
//        println("Turn music down a lil bit")
//
//        sampleMusic.play()
//        println("Music play!")

        //when I remove the comment of line 14, an error appears

//        println("createDefaultShader: before val vertexShader")
//
//        val vertexShader = """attribute vec4 ${ShaderProgram.POSITION_ATTRIBUTE};
//attribute vec4 ${ShaderProgram.COLOR_ATTRIBUTE};
//attribute vec2 ${ShaderProgram.TEXCOORD_ATTRIBUTE}0;
//uniform mat4 u_projTrans;
//varying vec4 v_color;
//varying vec2 v_texCoords;
//
//void main()
//{
//   v_color = ${ShaderProgram.COLOR_ATTRIBUTE};
//   v_color.a = v_color.a * (255.0/254.0);
//   v_texCoords = ${ShaderProgram.TEXCOORD_ATTRIBUTE}0;
//   gl_Position =  u_projTrans * ${ShaderProgram.POSITION_ATTRIBUTE};
//}
//"""
//        println("before val fragmentShader")
//
//        val fragmentShader = """#ifdef GL_ES
//#define LOWP lowp
//precision mediump float;
//#else
//#define LOWP
//#endif
//varying LOWP vec4 v_color;
//varying vec2 v_texCoords;
//uniform sampler2D u_texture;
//void main()
//{
//  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
//}"""
//
//        println("Before ShaderProgram(")
//        val shader = ShaderProgram(vertexShader, fragmentShader)
//
//        println("""isCompiled? ${shader.isCompiled}""")
//
//
//        println("createDefaultShader after")
        batch = SpriteBatch()
        println("SpriteBatch successfully instantiated")
        img = Texture(Gdx.files.internal("badlogic.jpg"))
        println("Texture successfully instantiated")
    }

    override fun render() {
        println("clearColor")
        Gdx.gl.glClearColor(1f, 1f, 0f, 1f)
        println("glClear")
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        println("batch.begin")
        batch.begin()
        println("batch.draw(img")
        batch.draw(img, 0f, 0f)
        println("batch.end")
        batch.end()
        println("batch ended")
    }

    override fun dispose() {
        sampleMusic.dispose()
        batch.dispose()
        img.dispose()
    }
}