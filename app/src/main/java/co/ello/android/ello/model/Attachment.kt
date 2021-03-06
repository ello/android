package co.ello.android.ello

import android.media.Image
import java.net.URL


data class Attachment(
    val url: URL
    ) : Model() {

    override val identifier = Parser.Identifier(id = url.toString(), table = MappingType.AttachmentsType)
    override fun update(property: Property, value: Any) {}

    var size: Int? = null
    var width: Int? = null
    var height: Int? = null
    var type: String? = null
    var image: Image? = null

    val isGif: Boolean get() = type == "image/gif" || url.getPath().endsWith(".gif")
}
