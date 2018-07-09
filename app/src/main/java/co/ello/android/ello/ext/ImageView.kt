package co.ello.android.ello

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.lang.Exception
import java.net.URL


fun ImageView.setImageURL(url: URL?, onSuccess: Block? = null, onError: Block? = null) {
    loadImageURL(url).into(this, object : Callback {
        override fun onSuccess() {
            onSuccess?.invoke()
        }
        override fun onError(e: Exception?) {
            onError?.invoke()
        }
    })
}

fun loadImageURL(url: URL?): RequestCreator = Picasso.get().load(url?.toString())

fun ImageView.setGifURL(url: URL?, onSuccess: Block? = null, onError: Block? = null) {
    Glide.with(this)
        .load(url?.toURI())
        .listener(object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                onSuccess?.invoke()
                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                onError?.invoke()
                return false
            }
        })
        .into(this)
}
