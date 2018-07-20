package co.ello.android.ello


data class Credentials(
    val accessToken: String,
    val refreshToken: String?,
    val isAnonymous: Boolean
) : Model() {
    override val identifier = null
    override fun update(property: Property, value: Any) {}
}
