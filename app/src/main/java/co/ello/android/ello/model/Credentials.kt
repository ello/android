package co.ello.android.ello


data class Credentials(
    val accessToken: String,
    val refreshToken: String?,
    val isAnonymous: Boolean
) : Model()
