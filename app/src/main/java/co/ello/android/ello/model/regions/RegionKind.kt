package co.ello.android.ello

import android.graphics.Region


enum class RegionKind(val value: String) {
    Text("text"),
    Image("image"),
    Embed("embed");

    companion object {
        fun create(value: String): RegionKind? = when(value) {
            "text" -> RegionKind.Text
            "image" -> RegionKind.Image
            "embed" -> RegionKind.Embed
            else -> null
        }
    }
}
