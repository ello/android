package co.ello.android.ello


class API {
    fun login(username: String, password: String): ElloRequest<Credentials> {
        val path = "/oauth/token"

        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path)
            .parser { gson, json ->
                gson.fromJson(json, Credentials::class.java)
            }

        elloRequest.addHeader("Accept", "application/json")
        elloRequest.addHeader("Content-Type", "application/json")
        elloRequest.body = mapOf(
            "email" to username,
            "password" to password,
            "client_id" to BuildConfig.NINJA_CLIENT_KEY,
            "client_secret" to BuildConfig.NINJA_CLIENT_SECRET,
            "grant_type" to "password"
        )

        return elloRequest
    }

    fun join(email: String, username: String, password: String): ElloRequest<Credentials> {
        val path = "/oauth/token"

        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path)
                .parser { gson, json ->
                    gson.fromJson(json, Credentials::class.java)
                }

        elloRequest.addHeader("Accept", "application/json")
        elloRequest.addHeader("Content-Type", "application/json")
        elloRequest.body = mapOf(
            "email" to email,
            "username" to username,
            "password" to password,
            "client_id" to BuildConfig.NINJA_CLIENT_KEY,
            "client_secret" to BuildConfig.NINJA_CLIENT_SECRET,
            "grant_type" to "password"
        )

        return elloRequest
    }
}
