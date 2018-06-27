package co.ello.android.ello

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewGroup
import android.widget.FrameLayout
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


class NavigationLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {

    private val contentLayout = FrameLayout(context)
    private val otherLayout = FrameLayout(context)
    private var state: State = State.Resting
    private var contentAnimator: ValueAnimator? = null
    private var otherAnimator: ValueAnimator? = null
    private var completion: CompletionBlock? = null

    enum class State {
        Resting, Popping, Pushing
    }

    private var contentView: View? = null
        set(value) {
            contentView?.let { contentLayout.removeView(it) }
            field = value
            if (value != null) {
                if (value == otherView)  otherLayout.removeView(value)
                contentLayout.addView(value)
            }
        }

    private var otherView: View? = null
        set(value) {
            otherView?.let { otherLayout.removeView(it) }
            field = value
            if (value != null) {
                if (value == contentView)  contentLayout.removeView(value)
                otherLayout.addView(value)
            }
        }

    init {
        addView(contentLayout)
        addView(otherLayout)
    }

    fun pop(view: View, animated: Boolean, completion: CompletionBlock? = null) {
        cancelAnimations()

        state = State.Popping
        otherView = contentView
        contentView = view

        if (animated) {
            otherLayout.visibility = View.VISIBLE
            contentAnimator = startAnimation(contentLayout, from = -measuredWidth.toFloat(), to = 0f)
            otherAnimator = startAnimation(otherLayout, from = -measuredWidth.toFloat(), to = 0f)
            this.completion = completion
        }
        else {
            completion?.invoke(true)
        }
    }

    fun push(view: View, animated: Boolean, completion: CompletionBlock? = null) {
        cancelAnimations()

        state = State.Pushing
        otherView = contentView
        contentView = view

        if (animated) {
            otherLayout.visibility = View.VISIBLE
            contentAnimator = startAnimation(contentLayout, from = measuredWidth.toFloat(), to = 0f)
            otherAnimator = startAnimation(otherLayout, from = measuredWidth.toFloat(), to = 0f)
            this.completion = completion
        }
        else {
            completion?.invoke(true)
        }
    }

    private fun cancelAnimations() {
        completed(false)

        contentAnimator?.cancel()
        contentAnimator = null
        contentLayout.translationX = 0f

        otherAnimator?.cancel()
        otherAnimator = null
        otherLayout.translationX = 0f
    }

    private fun startAnimation(layout: View, from: Float, to: Float): ValueAnimator {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.interpolator = LinearInterpolator()
        animator.duration = 300
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                if (animator == contentAnimator) {
                    contentAnimatorEnded()
                }
            }
        })
        animator.addUpdateListener(this)
        layout.translationX = from
        animator.start()

        return animator
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val view: View =
            if (animation == contentAnimator)  contentLayout
            else  otherLayout
        val progress = animation.animatedValue as Float
        view.translationX = progress
    }

    private fun contentAnimatorEnded() {
        completed(true)
        state = State.Resting
        otherView = null
        requestLayout()
    }

    private fun completed(finished: Boolean) {
        this.completion?.invoke(finished)
        this.completion = null
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val totalWidth = measuredWidth
        val totalHeight = measuredHeight
        contentLayout.measure(MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY))
        otherLayout.measure(MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY))

        contentLayout.layout(0, 0, totalWidth, totalHeight)

        if (state == State.Pushing) {
            otherLayout.layout(-totalWidth, 0, 0, totalHeight)
        }
        else if (state == State.Popping) {
            otherLayout.layout(totalWidth, 0, 2 * totalWidth, totalHeight)
        }
     }
}
