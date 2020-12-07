package com.badlogic.gdx.utils.reflect

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClassReflectionTest {
//     val listOfExampleClasses = mutableListOf("BitmapFont" ,"Color", "Skin.TintedDrawable")
    private val exceptedClasses = mutableListOf<String>("BitmapFont", "Color", "TintedDrawable")

    val listOfExampleClasses = mutableListOf<Class<*>>(BitmapFont::class.java, Color::class.java, Skin.TintedDrawable::class.java)

    @Test
    internal fun getSimpleName() {
        val listOfSimpleNameClasses : MutableList<String> = mutableListOf()
        for (i in listOfExampleClasses.withIndex()){
            listOfSimpleNameClasses.add(i.value.simpleName)
        }



        assertEquals(exceptedClasses, listOfSimpleNameClasses)
    }
}