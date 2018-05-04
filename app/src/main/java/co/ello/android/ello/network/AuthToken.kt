package co.ello.android.ello


class AuthToken(
    val token: String? = null,
    val type: Type = Type.None,
    val refreshToken: String? = null
    ) {

    enum class Type(val value: String?) {
        Password("password"), Anonymous("bearer"), None(null);

        companion object {
            fun create(value: String): Type = when(value) {
                "password" -> Type.Password
                "bearer" -> Type.Anonymous
                else -> Type.None
            }
        }
    }

    val isPresent: Boolean get() = token?.isNotEmpty() ?: false
    val tokenWithBearer: String? get() = this.token?.let { "Bearer $it" }

    companion object {
        var shared: AuthToken = AuthToken()
        var state: AuthState = AuthState.Initial

        fun update(credentials: Credentials) {
            val newToken = AuthToken(
                token = credentials.accessToken,
                type = Type.create(credentials.tokenType),
                refreshToken = credentials.refreshToken
                )
            AuthToken.shared = newToken
        }

        fun reset() {
            AuthToken.shared = AuthToken()
        }
    }
}
