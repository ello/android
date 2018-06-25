package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView


class SpinnerCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.spinner_cell, parent, false)) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val rotationAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    private var spinning = false

    init {
        rotationAnimation.duration = 250
        rotationAnimation.repeatCount = Animation.INFINITE
        rotationAnimation.repeatMode = Animation.RESTART
        rotationAnimation.interpolator = LinearInterpolator()
    }

    fun spin() {
        if (spinning)  return
        spinning = true
        imageView.startAnimation(rotationAnimation)
    }
}
