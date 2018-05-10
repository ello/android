package co.ello.android.ello

import android.view.ViewGroup
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import kotlin.math.roundToInt


class PostToolbar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val viewsButton = StyledButton(context, style = StyledButton.Style.Postbar)
    private val lovesButton = StyledButton(context, style = StyledButton.Style.Postbar)
    private val commentsButton = StyledButton(context, style = StyledButton.Style.Postbar)
    private val repostButton = StyledButton(context, style = StyledButton.Style.Postbar)
    private val shareButton = StyledButton(context, style = StyledButton.Style.Postbar)

    init {
        val drawables: List<Pair<StyledButton, Int>> = listOf(
            Pair(viewsButton, R.drawable.image_eye_button),
            Pair(lovesButton, R.drawable.image_heart_button),
            Pair(commentsButton, R.drawable.image_bubble_button),
            Pair(repostButton, R.drawable.image_repost_button),
            Pair(shareButton, R.drawable.image_share_button)
            )
        for ((button, res) in drawables) {
            val image = context.resources.getDrawable(res, context.theme)
            image.setBounds(0, 3.dp, 20.dp, 23.dp)
            button.setCompoundDrawables(image, null, null, null)
            button.setText("123")
        }

        addView(viewsButton)
        addView(lovesButton)
        addView(commentsButton)
        addView(repostButton)
        addView(shareButton)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val totalWidth = (measuredWidth - paddingLeft - paddingRight).toFloat()
        val childWidth = (totalWidth / if (childCount < 4) 4f else childCount.toFloat()).roundToInt()
        val childHeight = measuredHeight - paddingTop - paddingBottom

        var x = paddingLeft
        for (view in childViews) {
            view.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
            view.layout(x + (childWidth - view.measuredWidth) / 2, paddingTop, x + childWidth, paddingTop + childHeight)
            x += childWidth
        }
    }

}
