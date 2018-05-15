package co.ello.android.ello

import android.view.ViewGroup
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Button
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
        }

        addView(viewsButton)
        addView(lovesButton)
        addView(commentsButton)
        addView(repostButton)
        addView(shareButton)
    }

    var viewsCount: Count = Count.Hidden
        set(value) { updateCounts(viewsButton, value) }
    var lovesCount: Count = Count.Hidden
        set(value) { updateCounts(lovesButton, value) }
    var commentsCount: Count = Count.Hidden
        set(value) { updateCounts(commentsButton, value) }
    var repostCount: Count = Count.Hidden
        set(value) { updateCounts(repostButton, value) }

    var shareVisible: Boolean = true
        set(value) {
            field = value
            shareButton.visibility = if (value) View.VISIBLE else View.GONE
        }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val minViews = 3
        val totalWidth = (measuredWidth - paddingLeft - paddingRight).toFloat()
        val visibleCount: Int = childViews.filter { it.visibility == View.VISIBLE }.size
        val childWidth = (totalWidth / if (visibleCount < minViews) minViews.toFloat() else visibleCount.toFloat()).roundToInt()
        val childHeight = measuredHeight - paddingTop - paddingBottom

        var x = paddingLeft
        for (view in childViews) {
            if (view.visibility == View.GONE)  continue
            view.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
            view.layout(x + (childWidth - view.measuredWidth) / 2, paddingTop, x + childWidth, paddingTop + childHeight)
            x += childWidth
        }
    }

    private fun updateCounts(button: Button, value: Count) = when(value) {
        is Count.Hidden -> button.visibility = View.GONE
        is Count.Visible -> {
            button.visibility = View.VISIBLE
            button.setText(value.count.numberToHuman())
        }
    }

    fun setViewsButtonListener(fn: Block) {
        viewsButton.setOnClickListener { fn() }
    }

    fun setLovesButtonListener(fn: Block) {
        lovesButton.setOnClickListener { fn() }
    }

    fun setCommentsButtonListener(fn: Block) {
        commentsButton.setOnClickListener { fn() }
    }

    fun setRepostButtonListener(fn: Block) {
        repostButton.setOnClickListener { fn() }
    }

    fun setShareButtonListener(fn: Block) {
        shareButton.setOnClickListener { fn() }
    }

}
