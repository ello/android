package co.ello.android.ello

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView


class StyledLabel @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    enum class Style(val font: String = "Regular", val size: Float = 14f, val color: Int? = null, val background: Int? = null) {
        Default(color = R.color.black),
        White(color = R.color.white),

        SmallWhite(size = 12f, color = R.color.white),
        LargeWhite(size = 18f, color = R.color.white),
        LargeBoldWhite(font = "Bold", size = 18f, color = R.color.white)
    }

    var style: Style = Style.Default
        set(value: Style) {
            field = value
            updateStyle()
        }

    init {
        val styledAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.StyledButton, 0, 0)

        val styleName = styledAttrs.getString(R.styleable.StyledButton_styleName) ?: ""
        styledAttrs.recycle()

        this.style = when (styleName) {
            "white" -> Style.White
            "small white" -> Style.SmallWhite
            "large white" -> Style.LargeWhite
            "large bold white" -> Style.LargeBoldWhite
            else -> Style.Default
        }
    }

    private fun updateStyle() {
        this.typeface = Typeface.createFromAsset(context.assets, "AtlasGrotesk${style.font}.otf")
        this.setTextSize(style.size)
        style.color?.let { this.setTextColor(context.getColor(it)) }
    }

}
