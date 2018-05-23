package co.ello.android.ello

import android.media.Image
import java.net.URL


data class Attachment(
    val url: URL
    ) : Model() {

    var size: Int? = null
    var width: Int? = null
    var height: Int? = null
    var type: String? = null
    var image: Image? = null

    val isGif: Boolean get() = type == "image/gif" || url.getPath().endsWith(".gif")

    companion object {
        fun fromJSON(json: JSON): Attachment {
            val url = json["url"].url!!
            val attachment = Attachment(url)
            attachment.size = json["metadata"]["size"].int
            attachment.width = json["metadata"]["width"].int
            attachment.height = json["metadata"]["height"].int
            attachment.type = json["metadata"]["type"].string
            return attachment
        }
    }
}
