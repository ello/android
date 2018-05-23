package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView


class AppScreen : AppProtocols.Screen {
    override val contentView: View
    override val containerView: ViewGroup

    private val spinnerContainer: View
    private val spinnerImageView: ImageView

    constructor(activity: Activity) {
        contentView = activity.layoutInflater.inflate(R.layout.app_layout, null)

        containerView = contentView.findViewById(R.id.containerView)
        spinnerContainer = contentView.findViewById<View>(R.id.spinnerContainer)
        spinnerImageView = contentView.findViewById(R.id.spinner)

        val rotationAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotationAnimation.duration = 250
        rotationAnimation.repeatCount = Animation.INFINITE
        rotationAnimation.repeatMode = Animation.RESTART
        rotationAnimation.interpolator = LinearInterpolator()
        spinnerImageView.startAnimation(rotationAnimation)
    }

    override fun spinnerVisibility(visibile: Boolean) {
        spinnerContainer.visibility = if (visibile) View.VISIBLE else View.INVISIBLE

        if (visibile) {
            spinnerContainer.setFocusable(true)
            spinnerContainer.requestFocus()
        } else {
            spinnerContainer.isFocusable = false
            spinnerContainer.clearFocus()
        }
    }
}
