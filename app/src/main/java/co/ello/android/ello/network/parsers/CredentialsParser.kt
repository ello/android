package co.ello.android.ello


class CredentialsParser(val isAnonymous: Boolean) : Parser() {
    override fun parse(json: JSON): Credentials {
        return Credentials(
            accessToken = json["access_token"].stringValue,
            refreshToken = json["refresh_token"].string,
            isAnonymous = isAnonymous
        )
    }
}
