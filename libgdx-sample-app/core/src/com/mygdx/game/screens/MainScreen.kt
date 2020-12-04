package com.mygdx.game.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport


class MainScreen(val aGame: Game) : Screen {
    private val spriteBatch = SpriteBatch()
    private val stage = Stage(ScreenViewport())
    private val gameSkin = Skin(Gdx.files.internal("uiskin.json"));

    init {
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
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
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
    }
}