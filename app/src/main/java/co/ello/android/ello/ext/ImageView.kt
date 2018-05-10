package co.ello.android.ello

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.net.URL


fun ImageView.setImageURL(url: URL?) {
    loadImageURL(url).into(this)
}

fun loadImageURL(url: URL?): RequestCreator = Picasso.get().load(url?.toString())
