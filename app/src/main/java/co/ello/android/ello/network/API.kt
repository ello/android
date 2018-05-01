package co.ello.android.ello


class API {
    enum class CategoryFilter(val value: String) {
        Featured("FEATURED"),
        Trending("TRENDING"),
        Recent("RECENT"),
        Shop("SHOP");

        companion object {
            fun create(value: String): CategoryFilter? = when(value) {
                "FEATURED" -> CategoryFilter.Featured
                "TRENDING" -> CategoryFilter.Trending
                "RECENT" -> CategoryFilter.Recent
                "SHOP" -> CategoryFilter.Shop
                else -> null
            }
        }
    }

    fun globalPostStream(filter: CategoryFilter, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Post>>> {
        val request = GraphQLRequest<Pair<PageConfig, List<Post>>>("globalPostStream")
            .parser { json ->
                PageParser<Post>("posts", PostParser()).parse(json)
            }
            .setVariables(
                GraphQLRequest.Variable.enum("kind", filter.value, "StreamKind"),
                GraphQLRequest.Variable.optionalString("before", before)
            )
            .setFragments(Fragments.postStream)
            .setBody(Fragments.postStreamBody)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.TOKEN}")
        return request
    }

    fun login(username: String, password: String): ElloRequest<Credentials> {
        val path = "/api/oauth/token"

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
        val path = "/api/v2/join"

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
