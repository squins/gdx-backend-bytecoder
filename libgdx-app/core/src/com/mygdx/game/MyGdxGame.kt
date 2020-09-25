package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class MyGdxGame : ApplicationAdapter() {
    lateinit var  batch: SpriteBatch
    lateinit var img: Texture
    override fun create() {
        //when I remove the comment of line 14, an error appears
//        batch = SpriteBatch()
//        img = Texture("badlogic.jpg")
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
//        batch.begin()
//        batch.draw(img, 0f, 0f)
//        batch.end()
    }

    override fun dispose() {
//        batch.dispose()
//        img.dispose()
    }
}