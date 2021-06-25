import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip.TextTooltipStyle
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable
import org.junit.jupiter.api.Test


class TestNameInJson () {

    private val defaultTagClasses = arrayOf(BitmapFont::class.java, Color::class.java, Skin.TintedDrawable::class.java, NinePatchDrawable::class.java,
            SpriteDrawable::class.java, TextureRegionDrawable::class.java, TiledDrawable::class.java, Button.ButtonStyle::class.java,
            CheckBoxStyle::class.java, ImageButtonStyle::class.java, ImageTextButtonStyle::class.java,
            Label.LabelStyle::class.java, List.ListStyle::class.java, ProgressBarStyle::class.java, ScrollPane.ScrollPaneStyle::class.java,
            SelectBoxStyle::class.java, SliderStyle::class.java, SplitPaneStyle::class.java, TextButtonStyle::class.java,
            TextField.TextFieldStyle::class.java, TextTooltipStyle::class.java, TouchpadStyle::class.java, Tree.TreeStyle::class.java,
            Window.WindowStyle::class.java)
    @Test
    internal fun name() {
        defaultTagClasses.forEach {
            println(it::class.java.name)
        }
    }
}