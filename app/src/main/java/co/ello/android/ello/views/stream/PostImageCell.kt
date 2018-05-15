package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import java.net.URL
import kotlin.math.roundToInt


class PostImageCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.post_image_cell, parent, false)) {
    val imageView: ImageView

    data class Config(
        val imageURL: URL
        )

    init {
        imageView = itemView.findViewById(R.id.imageView)
    }

    var config: Config? = null
        set(value) {
            streamCellItem?.height?.let { assignHeight(it) }
            imageView.setImageURL(value?.imageURL, onSuccess = { calculateHeight() }, onError = {
                println("error: ${value?.imageURL}")
            })
        }

    private fun assignHeight(height: Int) {
        imageView.layoutParams = FrameLayout.LayoutParams(imageView.measuredWidth, height)
    }

    private fun calculateHeight() {
        val imageWidth = imageView.drawable.intrinsicWidth
        val imageHeight = imageView.drawable.intrinsicHeight
        if (imageWidth > 0 && imageHeight > 0) {
            val aspectRatio = imageWidth.toFloat() / imageHeight.toFloat()
            val width = imageView.measuredWidth
            val height = (width.toFloat() / aspectRatio).roundToInt()

            if (width > 0 && streamCellItem?.height != height) {
                streamCellItem?.height = height
                assignHeight(height)
            }
        }
    }
}
