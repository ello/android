package co.ello.android.ello

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView


class StyledButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    enum class Corner {
        Pill,
        Rounded,
        None
    }

    enum class Alignment {
        Left, Center
    }

    enum class Style(
            val font: String = "Regular",
            val size: Float = 14f,
            val color: Int,
            val highlightedColor: Int? = null,
            val backgroundColor: Int? = null,
            val corners: Corner = Corner.None,
            val textAlign: Alignment? = null,
            val customization: ((StyledButton) -> Unit) = {}
    ) {
        Default(color = R.color.black),

        Green(color = R.color.white, highlightedColor = R.color.greyA, backgroundColor = R.color.green, corners = Corner.Rounded),
        White(color = R.color.black, backgroundColor = R.color.white),

        BlackPill(color = R.color.white, highlightedColor = R.color.greyA, backgroundColor = R.color.black, corners = Corner.Pill, textAlign = Alignment.Center),
        GrayPill(color = R.color.white, highlightedColor = R.color.black, backgroundColor = R.color.greyA, corners = Corner.Pill, textAlign = Alignment.Center),
        GreenPill(color = R.color.white, highlightedColor = R.color.greyA, backgroundColor = R.color.green, corners = Corner.Pill, textAlign = Alignment.Center),
        RedPill(color = R.color.white, highlightedColor = R.color.greyA, backgroundColor = R.color.red, corners = Corner.Pill, textAlign = Alignment.Center),

        ClearWhite(color = R.color.white),
        ClearGray(color = R.color.greyA, highlightedColor = R.color.black),
        ClearBlack(color = R.color.black),

        ForgotPassword(size = 11f, color = R.color.greyA, highlightedColor = R.color.white),
        Postbar(color = R.color.greyA, highlightedColor = R.color.black, textAlign = Alignment.Center, customization = { button ->
            button.setCompoundDrawablePadding(5.dp)
        }),

        RoundedGrayOutline(color = R.color.greyA, highlightedColor = R.color.black, corners = Corner.Rounded)
    }

    var style: Style = Style.Default
        set(value) {
            field = value
            updateStyle()
        }

    // these need to be calculated and stored from context.getColor
    var color: Int = R.color.black
    var highlightedColor: Int? = null
    var backgroundColor: Int? = null

    constructor(context: Context, style: Style) : this(context) {
        this.style = style
    }

    init {
        isClickable = true
        val styledAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.StyledButton, 0, 0)
        val styleName = styledAttrs.getString(R.styleable.StyledButton_styleName) ?: ""
        styledAttrs.recycle()

        this.style = when (styleName) {
            "clear white"          -> Style.ClearWhite
            "clear gray"           -> Style.ClearGray
            "clear black"          -> Style.ClearBlack
            "black pill"           -> Style.BlackPill
            "gray pill"            -> Style.GrayPill
            "green pill"           -> Style.GreenPill
            "red pill"             -> Style.RedPill
            "green"                -> Style.Green
            "forgot password"      -> Style.ForgotPassword
            "white"                -> Style.White
            "rounded gray outline" -> Style.RoundedGrayOutline
            else                   -> Style.Default
        }
    }

    fun updateStyle() {
        this.typeface = Typeface.createFromAsset(context.assets, "AtlasGrotesk${style.font}.otf")
        this.setTextSize(style.size.fontDp)

        val statesList = ArrayList<IntArray>()
        val colorsList = ArrayList<Int>()

        style.highlightedColor?.let { highlightedColor ->
            statesList.add(intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed))
            colorsList.add(context.getColor(highlightedColor))
        }

        statesList.add(intArrayOf(android.R.attr.state_enabled))
        colorsList.add(context.getColor(style.color))

        val states = Array<IntArray>(statesList.size) { statesList[it] }
        val colors = IntArray(colorsList.size) { colorsList[it] }
        setTextColor(ColorStateList(states, colors))

        style.textAlign?.let { when(it) {
            Alignment.Left -> {
                this.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                this.gravity = Gravity.CENTER
            }
            Alignment.Center -> {
                this.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                this.gravity = Gravity.CENTER
            }
        } }
        setPadding(0, 0, 0, 3.dp)

        color = context.getColor(style.color)
        highlightedColor = style.highlightedColor?.let { context.getColor(it) }
        backgroundColor = style.backgroundColor?.let { context.getColor(it) }

        style.customization(this)
    }

    override fun onDraw(canvas_: Canvas?) {
        val backgroundColor = this.backgroundColor

        canvas_?.let { canvas ->
            val p = Paint()
            p.strokeWidth = 1.dpf

            val rect = RectF(paddingStart.toFloat(), paddingTop.toFloat(), measuredWidth.toFloat(), measuredHeight.toFloat())

            when(style.corners) {
                Corner.Pill -> {
                    if (backgroundColor != null) {
                        p.setColor(backgroundColor)
                        p.style = Paint.Style.FILL_AND_STROKE
                    }
                    else {
                        p.setColor(if (isPressed) highlightedColor ?: style.color else style.color)
                        p.style = Paint.Style.STROKE
                    }

                    val minDim = if (measuredWidth > measuredHeight) measuredHeight else measuredWidth
                    val radius = minDim.toFloat() / 2f
                    canvas.drawRoundRect(rect, radius, radius, p)
                }
                Corner.None -> {
                    if (backgroundColor != null) {
                        p.setColor(backgroundColor)
                        p.style = Paint.Style.FILL_AND_STROKE
                        canvas.drawRect(rect, p)
                    }
                }
                Corner.Rounded -> {
                    if (backgroundColor != null) {
                        p.setColor(backgroundColor)
                        p.style = Paint.Style.FILL_AND_STROKE
                    }
                    else {
                        p.setColor(if (isPressed) highlightedColor ?: style.color else style.color)
                        p.style = Paint.Style.STROKE
                    }

                    if (backgroundColor == null) {
                        p.style = Paint.Style.STROKE
                    }
                    else {
                        p.style = Paint.Style.FILL_AND_STROKE
                    }

                    val five = 5.dpf
                    canvas.drawRoundRect(rect, five, five, p)
                }
            }
        }
        super.onDraw(canvas_)
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
