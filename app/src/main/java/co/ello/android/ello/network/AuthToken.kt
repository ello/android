package co.ello.android.ello

import com.orhanobut.hawk.Hawk


class AuthToken(
    val token: String? = null,
    val type: Type = Type.None,
    val refreshToken: String? = null
    ) {

    enum class Type {
        Password, Anonymous, None
    }

    val isPresent: Boolean get() = token != null && !token.isEmpty()
    val tokenWithBearer: String? get() = this.token?.let { "Bearer $it" }

    companion object {
        var shared: AuthToken = AuthToken()
        var state: AuthState = AuthState.Initial

        fun init() {
            val accessToken: String? = Hawk.get("Token.accessToken")
            val isAnonymous: Boolean = Hawk.get("Token.isAnonymous") ?: true
            val refreshToken: String? = Hawk.get("Token.refreshToken")

            if (accessToken != null) {
                val newToken = AuthToken(
                    token = accessToken,
                    type = if (isAnonymous) Type.Anonymous else Type.Password,
                    refreshToken = refreshToken
                    )
                AuthToken.shared = newToken
            }
        }

        fun update(credentials: Credentials) {
            Hawk.put("Token.accessToken", credentials.accessToken)
            Hawk.put("Token.isAnonymous", credentials.isAnonymous)
            Hawk.put("Token.refreshToken", credentials.refreshToken)

            val newToken = AuthToken(
                token = credentials.accessToken,
                type = if (credentials.isAnonymous) Type.Anonymous else Type.Password,
                refreshToken = credentials.refreshToken
                )
            AuthToken.shared = newToken
        }

        fun reset() {
            AuthToken.shared = AuthToken()
        }
    }
}
