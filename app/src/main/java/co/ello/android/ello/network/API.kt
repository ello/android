package co.ello.android.ello


class API {
    enum class CategoryFilter(val value: String) {
        FEATURED("FEATURED"),
        TRENDING("TRENDING"),
        RECENT("RECENT"),
        SHOP("SHOP")
    }

    fun globalPostStream(filter: CategoryFilter, before: String? = null): GraphQLRequest<String> { // (PageConfig, [Post])
        val request = GraphQLRequest<String>("globalPostStream")
            .parser { json ->
                //PageParser<Post>("posts", PostParser()).parse(json)
                print("json:\n$json\n")
                ""
            }
            .setVariables(
                GraphQLRequest.Variable.enum("kind", "StreamKind", filter.value),
                GraphQLRequest.Variable.optionalString("before", before)
            )
            .setFragments(Fragments.postStream)
            .setBody(Fragments.postStreamBody)
        return request
    }

    fun login(username: String, password: String): ElloRequest<Credentials> {
        val path = "${BuildConfig.NINJA_DOMAIN}/api/oauth/token"

        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path)
            .parser { gson, json ->
                gson.fromJson(json, Credentials::class.java)
            }
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .setBody(mapOf(
                "email" to username,
                "password" to password,
                "client_id" to BuildConfig.NINJA_CLIENT_KEY,
                "client_secret" to BuildConfig.NINJA_CLIENT_SECRET,
                "grant_type" to "password"
            ))

        return elloRequest
    }

    fun join(email: String, username: String, password: String): ElloRequest<Credentials> {
        val path = "${BuildConfig.NINJA_DOMAIN}/api/v2/join"

        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path)
                .parser { gson, json ->
                    println("join $json")
                    gson.fromJson(json, Credentials::class.java)
                }
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .setBody(mapOf(
                    "email" to email,
                    "username" to username,
                    "password" to password,
                    "client_id" to BuildConfig.NINJA_CLIENT_KEY,
                    "client_secret" to BuildConfig.NINJA_CLIENT_SECRET,
                    "grant_type" to "password"
                ))

        return elloRequest
    }
}
