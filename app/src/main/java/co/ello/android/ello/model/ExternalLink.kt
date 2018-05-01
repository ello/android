package co.ello.android.ello

import java.net.URL


data class ExternalLink(
        val url: URL,
        val text: String,
        val iconURL: URL?
    ) {
}
