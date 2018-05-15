package co.ello.android.ello

import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView


class StartupScreen : StartupProtocols.Screen {
    override val contentView: ConstraintLayout
    private val spinnerImageView: ImageView

    constructor(activity: Activity) {
        contentView = activity.layoutInflater.inflate(R.layout.startup_layout, null) as ConstraintLayout

        spinnerImageView = contentView.findViewById<ImageView>(R.id.spinner)
        val rotationAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotationAnimation.duration = 250
        rotationAnimation.repeatCount = Animation.INFINITE
        rotationAnimation.repeatMode = Animation.RESTART
        rotationAnimation.interpolator = LinearInterpolator()
        spinnerImageView.startAnimation(rotationAnimation)
    }
}
