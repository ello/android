package co.ello.android.ello

import android.widget.ImageView
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
