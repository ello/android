package co.ello.android.ello

import java.net.URL
import android.net.Uri

fun URL.toUri(): Uri? {
    return Uri.parse(this.toString())
}