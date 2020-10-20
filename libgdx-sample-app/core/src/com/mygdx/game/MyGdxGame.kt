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

        sampleMusic = Gdx.audio.newMusic(fileReference)
        println("Music connected to file, now play")

        sampleMusic.setOnCompletionListener {  }

        sampleMusic.isLooping = true
        println("looping is true")

        sampleMusic.volume = 0.5f
        println("Turn music down a lil bit")

        sampleMusic.play()
        println("Music play!")

        //when I remove the comment of line 14, an error appears

        SpriteBatch.createDefaultShader()
//        batch = SpriteBatch()
//        img = Texture(Gdx.files.internal("badlog" +
//                "ic.jpg"))
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
//        batch.begin()
//        batch.draw(img, 0f, 0f)
//        batch.end()
    }

    override fun dispose() {
        sampleMusic.dispose()
        batch.dispose()
        img.dispose()
    }
}