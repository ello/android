package co.ello.android.ello

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.widget.EditText


class StyledTextInput @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : EditText(context, attrs, defStyleAttr) {

    enum class KeyboardType {
        Default,
        Email,
        Password
    }

    enum class Background {
        BottomLine,
        None
    }

    enum class Style(
        val font: String = "Regular",
        val size: Float = 14f,
        val color: Int? = null,
        val placeholder: Int? = null,
        val background: Background = Background.None,
        val keyboardType: KeyboardType = KeyboardType.Default,
        val isMultiline: Boolean = false
    ) {
        Default(color = R.color.black),
        White(color = R.color.white),
        CredentialsUsername(size = 18f, color = R.color.white, placeholder = R.color.white, background = Background.BottomLine, keyboardType = KeyboardType.Email),
        CredentialsPassword(size = 18f, color = R.color.white, placeholder = R.color.white, background = Background.BottomLine, keyboardType = KeyboardType.Password)
    }

    val style: Style

    init {
        val styledAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.StyledButton, 0, 0)

        val styleName = styledAttrs.getString(R.styleable.StyledButton_styleName) ?: ""
        styledAttrs.recycle()

        this.style = when (styleName) {
            "white" -> Style.White
            "credentials username" -> Style.CredentialsUsername
            "credentials password" -> Style.CredentialsPassword
            "default", "" -> Style.Default
            else -> {
                println("unknown style \"$styleName\"")
                Style.Default
            }
        }
        this.background = null

        updateStyle()
    }

    fun updateStyle() {
        this.typeface = Typeface.createFromAsset(context.assets, "AtlasGrotesk${style.font}.otf")
        this.setTextSize(style.size * 1.2f)
        style.color?.let { this.setTextColor(context.getColor(it)) }
        style.placeholder?.let { this.setHintTextColor(context.getColor(it)) }
        this.setSingleLine(!style.isMultiline)

        when (style.keyboardType) {
            KeyboardType.Default -> {
                this.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                this.transformationMethod = null
            }
            KeyboardType.Email -> {
                this.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                this.transformationMethod = null
            }
            KeyboardType.Password -> {
                this.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_PASSWORD);
                this.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            if (style.background == Background.BottomLine) {
                val p = Paint()
                p.style = Paint.Style.STROKE
                p.strokeWidth = resources.displayMetrics.density
                p.setColor(Color.WHITE)
                it.drawLine(0f, measuredHeight.toFloat(), measuredWidth.toFloat(), measuredHeight.toFloat(), p)
            }
        }
    }

}
