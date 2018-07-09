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
        Bandcamp("bandcamp"),
        Gif("gif");

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
                "gif" -> Service.Gif
                else -> null
            }
        }
    }
}
