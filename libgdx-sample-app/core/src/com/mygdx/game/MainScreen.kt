package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport


class MainScreen(val aGame: Game) : Screen {
    private val spriteBatch:SpriteBatch
    private val stage:Stage
    private val gameSkin: Skin
//    private val atlas : TextureAtlas
//    private val img : Texture

    init {
        println("fileReference")
        val skin = Gdx.files.internal("uiskin.json")
        println("TextureAtlas")
//        atlas = TextureAtlas("uiskin.atlas")
        println("gameSkin")
        gameSkin = Skin(skin);
        println("SpriteBatch()")
        spriteBatch = SpriteBatch()
        println("ScreenViewport()")
        val screenViewport = ScreenViewport()
        println("Stage(screenViewport)")
        stage = Stage(screenViewport)

//        img = Texture(Gdx.files.internal("badlogic.jpg"))

        val title = Label("Title Screen", gameSkin, "default")
        title.setAlignment(Align.center)
        title.y = Gdx.graphics.height * 2/3.toFloat()
        title.width = Gdx.graphics.width.toFloat()
        stage.addActor(title)

//        val playButton = TextButton("Play!", gameSkin)
//        playButton.width = (Gdx.graphics.width / 2).toFloat()
//        playButton.setPosition(Gdx.graphics.width / 2 - playButton.width / 2, Gdx.graphics.height / 2 - playButton.height / 2)
//        stage.addActor(playButton)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(.135f, .206f, .235f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
//        spriteBatch.begin()
//        // DISABLED: performance println("batch.draw(img")
//        spriteBatch.draw(img, 0f, 0f)
//        // DISABLED: performance println("batch.end")
//        spriteBatch.end()
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        stage.dispose()
//        atlas.dispose()
//        spriteBatch.dispose()
    }
}