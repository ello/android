package co.ello.android.ello

import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import java.net.URL
import kotlin.math.roundToInt


class PostImageCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.post_image_cell, parent, false)) {
    private val imageView: ImageView
    private var aspectRatio: Float? = null

    data class Config(
        val imageURL: URL
        )

    init {
        imageView = itemView.findViewById(R.id.imageView)
        imageView.viewTreeObserver.addOnGlobalLayoutListener { didResize() }
    }

    var config: Config? = null
        set(value) {
            aspectRatio = null
            streamCellItem?.height?.let { assignHeight(it) }
            imageView.setImageURL(value?.imageURL, onSuccess = { calculateHeight() }, onError = {
                println("error: ${value?.imageURL}")
            })
        }

    private fun assignHeight(height: Int) {
        itemView.layoutParams.height = height
        imageView.layoutParams = ConstraintLayout.LayoutParams(imageView.measuredWidth, height)
    }

    private fun calculateHeight() {
        val imageWidth = imageView.drawable.intrinsicWidth
        val imageHeight = imageView.drawable.intrinsicHeight
        if (imageWidth > 0 && imageHeight > 0) {
            aspectRatio = imageWidth.toFloat() / imageHeight.toFloat()
            didResize()
        }
    }

    private fun didResize() {
        val aspectRatio = this.aspectRatio ?: return
        val width = imageView.measuredWidth
        val height = (width.toFloat() / aspectRatio).roundToInt()

        if (width > 0 && streamCellItem?.height != height) {
            streamCellItem?.height = height
            assignHeight(height)
        }
    }
}
