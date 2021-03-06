package co.ello.android.ello

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView


class StyledLabel @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    enum class Style(
            val font: String = "Regular",
            val size: Float = 14f,
            val color: Int? = null,
            val background: Int? = null,
            val underline: Boolean = false
    ) {
        Default(color = R.color.black),
        White(color = R.color.white),
        Gray(color = R.color.greyA),
        SmallGray(size = 12f, color = R.color.greyA),
        BoldWhite(font = "Bold", color = R.color.white),
        BoldWhiteUnderline(font = "Bold", color = R.color.white, underline = true),

        SmallWhite(size = 12f, color = R.color.white),
        StatsCount(size = 18f, color = R.color.black),
        StatsCaption(size = 10f, color = R.color.greyA),
        LargeWhite(size = 18f, color = R.color.white),
        LargeBlack(size = 18f, color = R.color.black),
        LargeBoldWhite(font = "Bold", size = 18f, color = R.color.white)
    }

    var style: Style = Style.Default
        set(value) {
            field = value
            updateStyle()
        }

    init {
        val styledAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.StyledButton, 0, 0)

        val styleName = styledAttrs.getString(R.styleable.StyledButton_styleName) ?: ""
        styledAttrs.recycle()

        this.style = when (styleName) {
            "white" -> Style.White
            "gray" -> Style.Gray
            "bold white" -> Style.BoldWhite
            "small white" -> Style.SmallWhite
            "stats count" -> Style.StatsCount
            "stats caption" -> Style.StatsCaption
            "small gray" -> Style.SmallGray
            "large black" -> Style.LargeBlack
            "large white" -> Style.LargeWhite
            "large bold white" -> Style.LargeBoldWhite
            else -> Style.Default
        }
    }

    private fun updateStyle() {
        this.typeface = Typeface.createFromAsset(context.assets, "AtlasGrotesk${style.font}.otf")
        this.setTextSize(style.size.fontDp)
        this.paintFlags =
                if (style.underline)
                    this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                else
                    this.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        style.color?.let { this.setTextColor(context.getColor(it)) }
    }

}
