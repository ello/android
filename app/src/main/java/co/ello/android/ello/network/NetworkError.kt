package co.ello.android.ello


class NetworkError(json: JSON): Throwable() {
    val reason: String? = json["errors"]["title"].string
    val status: String? = json["errors"]["status"].string
}
