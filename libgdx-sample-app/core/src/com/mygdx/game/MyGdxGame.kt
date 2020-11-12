package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch


class MyGdxGame : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var img: Texture
    lateinit var img2: Texture
    lateinit var sampleMusic: Music

    override fun create() {
        // DISABLED: performance println("Create")
        val fileReference = Gdx.files.internal("sample.mp3")
        // DISABLED: performance println("Created fileReference")

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

        batch = SpriteBatch()
        // DISABLED: performance println("SpriteBatch successfully instantiated")
        img = Texture(Gdx.files.internal("badlogic.jpg"))

        img2 = Texture(Gdx.files.internal("badlogic1.jpg"))
        // DISABLED: performance println("Texture successfully instantiated")
    }

    override fun render() {
        // DISABLED: performance println("clearColor")
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        // DISABLED: performance println("glClear")
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        // DISABLED: performance println("batch.begin")
        batch.begin()
        // DISABLED: performance println("batch.draw(img")
        batch.draw(img, 0f, 0f)

        batch.draw(img2, 50f, 50f)
        // DISABLED: performance println("batch.end")
        batch.end()
        // DISABLED: performance println("batch ended")
    }

    override fun dispose() {
        sampleMusic.dispose()
        batch.dispose()
        img.dispose()
    }
}