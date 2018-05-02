package co.ello.android.ello

import java.net.URL


data class EmbedRegion(
    val id: String,
    val service: Service,
    val url: URL,
    val thumbnailLargeUrl: URL?
    ) : Model(), Regionable {

    override var isRepost: Boolean = false
    override val kind: RegionKind get() = RegionKind.Embed

    val isAudioEmbed: Boolean get() =
        service == Service.Mixcloud ||
        service == Service.Soundcloud ||
        service == Service.Bandcamp

    enum class Service(val value: String) {
        Codepen("codepen"),
        Dailymotion("dailymotion"),
        Mixcloud("mixcloud"),
        Soundcloud("soundcloud"),
        Youtube("youtube"),
        Vimeo("vimeo"),
        UStream("ustream"),
        Bandcamp("bandcamp");

        companion object {
            fun create(value: String): Service? = when(value) {
                "codepen" -> Service.Codepen
                "dailymotion" -> Service.Dailymotion
                "mixcloud" -> Service.Mixcloud
                "soundcloud" -> Service.Soundcloud
                "youtube" -> Service.Youtube
                "vimeo" -> Service.Vimeo
                "ustream" -> Service.UStream
                "bandcamp" -> Service.Bandcamp
                else -> null
            }
        }
    }

    companion object {
        fun fromJSON(json: JSON): EmbedRegion? {
            val id = json["data"]["id"].string ?: return null
            val service = json["data"]["service"].string?.let { Service.create(it) } ?: return null
            val url = json["data"]["url"].url ?: return null
            val thumbnailLargeUrl = json["data"]["thumbnailLargeUrl"].url

            return EmbedRegion(
                id = id,
                service = service,
                url = url,
                thumbnailLargeUrl = thumbnailLargeUrl
            )
        }
    }
}
