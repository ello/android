package co.ello.android.ello

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.graphics.drawable.shapes.RoundRectShape
import android.view.Gravity
import android.view.MotionEvent


class StyledButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    enum class Corner {
        Pill,
        Rounded
    }

    enum class Alignment {
        Left, Center
    }

    enum class Style(
            val font: String = "Regular",
            val size: Float = 14f,
            val color: Int? = null,
            val highlightedColor: Int? = null,
            val background: Int? = null,
            val cornerRadius: Corner? = null,
            val textAlign: Alignment = Alignment.Center
    ) {

        Default(color = R.color.white, background = R.color.black),
        ClearWhite(color = R.color.white),
        ClearBlack(color = R.color.black),
        ForgotPassword(size = 11f, color = R.color.gray, highlightedColor = R.color.white),
        White(color = R.color.black, background = R.color.white),
        RoundedGrayOutline(color = R.color.greyA, highlightedColor = R.color.black, cornerRadius = Corner.Rounded)

    }

    val style: Style
    // these need to be calculated and stored from context.getColor
    var color: Int?
    var highlightedColor: Int?

    init {
        isClickable = true
        val styledAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.StyledButton, 0, 0)
        val styleName = styledAttrs.getString(R.styleable.StyledButton_styleName) ?: ""
        styledAttrs.recycle()

        this.style = when (styleName) {
            "clear white" -> Style.ClearWhite
            "clear black" -> Style.ClearBlack
            "forgot password" -> Style.ForgotPassword
            "white" -> Style.White
            "rounded gray outline" -> Style.RoundedGrayOutline
            else -> Style.Default
        }

        this.typeface = Typeface.createFromAsset(context.assets, "AtlasGrotesk${style.font}.otf")
        this.setTextSize(style.size)

        style.color?.let { color ->
            val statesList = ArrayList<IntArray>()
            val colorsList = ArrayList<Int>()

            style.highlightedColor?.let { highlightedColor ->
                statesList.add(intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed))
                colorsList.add(context.getColor(highlightedColor))
            }

            statesList.add(intArrayOf(android.R.attr.state_enabled))
            colorsList.add(context.getColor(color))

            val states = Array<IntArray>(statesList.size) { statesList[it] }
            val colors = IntArray(colorsList.size) { colorsList[it] }
            setTextColor(ColorStateList(states, colors))
        }

        when(style.textAlign) {
            Alignment.Left -> {
                this.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                this.gravity = Gravity.LEFT
            }
            Alignment.Center -> {
                this.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                this.gravity = Gravity.CENTER
            }
        }

        if (style.color != null) {
            color = context.getColor(style.color)
        }
        else {
            color = null
        }

        if (style.highlightedColor != null) {
            highlightedColor = context.getColor(style.highlightedColor)
        }
        else {
            highlightedColor = null
        }
    }

    override fun onDraw(canvas_: Canvas?) {
        super.onDraw(canvas_)

        safeLet(style.cornerRadius, canvas_, color) { cornerRadius, canvas, color ->
            when(cornerRadius) {
                Corner.Pill -> {}
                Corner.Rounded -> {
                    val p = Paint()
                    p.style = Paint.Style.STROKE
                    p.strokeWidth = 1f * resources.displayMetrics.density
                    if (isPressed) {
                        p.setColor(highlightedColor ?: color)
                    } else {
                        p.setColor(color)
                    }

                    val five = 5f * resources.displayMetrics.density
                    val rect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
                    canvas.drawRoundRect(rect, five, five, p)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val ret = super.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> invalidate()
            else -> {}
        }
        return ret
    }

}
