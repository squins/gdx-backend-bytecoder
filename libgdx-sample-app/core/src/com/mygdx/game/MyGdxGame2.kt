package com.mygdx.game

import com.badlogic.gdx.Game
import com.mygdx.game.screens.MainScreen

class MyGdxGame2 : Game() {
    override fun create() {
        this.setScreen(MainScreen(this))
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        super.dispose()
    }
}