package co.ello.android.ello

import java.net.URL


data class ImageRegion(
    val url: URL,
    val buyButtonURL: URL?
    ) : Model(), Regionable {

    override var isRepost: Boolean = false
    override val kind: RegionKind get() = RegionKind.Image

    val asset: Asset? get() = getLinkObject("assets")
    val fullScreenURL: URL? get() {
        val asset = this.asset ?: return url

        val assetURL: URL?
        if (asset.isGif) { assetURL = asset.optimized?.url }
        else { assetURL = asset.oneColumnAttachment?.url }

        return assetURL ?: url
    }
}
