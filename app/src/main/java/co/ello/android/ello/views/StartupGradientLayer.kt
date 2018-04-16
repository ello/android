package co.ello.android.ello

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.drawable.GradientDrawable


const val NUM_COLORS = 11

class StartupGradientLayer : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                context.getColor(R.color.credentialsGradient_1),
                context.getColor(R.color.credentialsGradient_2)
            ))
        background = gradientDrawable
    }

}
