package co.ello.android.ello

import java.net.URL


data class ImageRegion(
    val url: URL,
    val buyButtonURL: URL
    ) : Model(), Regionable {

    override var isRepost: Boolean = false
    override val kind: RegionKind get() = RegionKind.Image

    val asset: Asset? get() = getLinkObject("assets")
    val fullScreenURL: URL? get() {
        val asset: Asset = this.asset ?: return url

        val assetURL: URL?
        if (asset.isGif) { assetURL = asset.optimized?.url }
        else { assetURL = asset.oneColumnAttachment?.url }

        return assetURL ?: url
    }

    companion object {
        fun fromJSON(json: JSON): ImageRegion? {
            val buyButtonURL: URL = json["linkUrl"].string?.let { URL(it) } ?: return null
            val url: URL = json["data"]["url"].string?.let { URL(it) } ?: return null
            val imageRegion = ImageRegion(url = url, buyButtonURL = buyButtonURL)

            json["links"]["assets"].string?.let { id ->
                imageRegion.addLinkObject("assets", id = id, type = MappingType.assetsType)
            }

            return imageRegion
        }
    }
}
