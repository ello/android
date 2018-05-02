package co.ello.android.ello

import java.net.URL


data class ExternalLink(
        val url: URL,
        val text: String,
        val iconURL: URL?
    ) {

    companion object {
        fun fromJSON(json: JSON): ExternalLink? {
            return safeLet(json["url"].string, json["text"].string) { url, text ->
                ExternalLink(URL(url), text, json["icon"].url)
            }
        }
    }
}
