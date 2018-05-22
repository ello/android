package co.ello.android.ello


class API {
    companion object {
        fun init() {
            key = BuildConfig.RAINBOW_CLIENT_KEY
            secret = BuildConfig.RAINBOW_CLIENT_SECRET
            domain = BuildConfig.RAINBOW_DOMAIN
        }

        var key: String = ""
        var secret: String = ""
        var domain: String = ""
    }

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
        return GraphQLRequest<Pair<PageConfig, List<Post>>>("globalPostStream")
            .parser { json ->
                PageParser<Post>("posts", PostParser()).parse(json)
            }
            .setVariables(
                GraphQLRequest.Variable.enum("kind", filter.value, "StreamKind"),
                GraphQLRequest.Variable.optionalString("before", before)
            )
            .setBody(Fragments.postStreamBody)
    }

    fun subscribedPostStream(filter: CategoryFilter, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Post>>> {
        return GraphQLRequest<Pair<PageConfig, List<Post>>>("subscribedPostStream")
            .parser { json ->
                PageParser<Post>("posts", PostParser()).parse(json)
            }
            .setVariables(
                GraphQLRequest.Variable.enum("kind", filter.value, "StreamKind"),
                GraphQLRequest.Variable.optionalString("before", before)
            )
            .setBody(Fragments.postStreamBody)
    }

    fun categoryPostStream(filter: CategoryFilter, category: Token, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Post>>> {
        val categoryVar: GraphQLRequest.Variable = when(category) {
            is ID -> GraphQLRequest.Variable.optionalInt("id", category.id.toInt())
            is Slug -> GraphQLRequest.Variable.optionalInt("slug", category.slug.toInt())
        }

        return GraphQLRequest<Pair<PageConfig, List<Post>>>("categoryPostStream")
                .parser { json ->
                    PageParser<Post>("posts", PostParser()).parse(json)
                }
                .setVariables(
                        GraphQLRequest.Variable.enum("kind", filter.value, "StreamKind"),
                        categoryVar,
                        GraphQLRequest.Variable.optionalString("before", before)
                )
                .setBody(Fragments.postStreamBody)
    }

    fun subscribedCategories(): GraphQLRequest<List<Category>> {
        return GraphQLRequest<List<Category>>("categoryNav")
            .parser { json ->
                ManyParser<Category>(CategoryParser()).parse(json)
            }
            .setBody(Fragments.categoriesBody)
    }

    fun editorialStream(before: String? = null): GraphQLRequest<Pair<PageConfig, List<Editorial>>> {
        return GraphQLRequest<Pair<PageConfig, List<Editorial>>>("editorialStream")
            .parser { json ->
                PageParser<Editorial>("editorials", EditorialParser()).parse(json)
            }
            .setVariables(
                GraphQLRequest.Variable.optionalString("before", before),
                GraphQLRequest.Variable.optionalBoolean("preview", false),
                GraphQLRequest.Variable.optionalInt("perPage", null)
            )
            .setBody(Fragments.editorialsBody)
    }

    fun join(email: String, username: String, password: String): ElloRequest<Credentials> {
        val path = "/api/v2/join"

        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path)
                .parser { gson, json ->
                    println("join $json")
                    gson.fromJson(json, Credentials::class.java)
                }
                .setBody(mapOf(
                    "email" to email,
                    "username" to username,
                    "password" to password,
                    "client_id" to API.key,
                    "client_secret" to API.secret,
                    "grant_type" to "password"
                ))

        return elloRequest
    }

    fun login(username: String, password: String): ElloRequest<Credentials> {
        val path = "/api/oauth/token"

        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path, requiresAnyToken = false, supportsAnonymousToken = true)
            .parser { gson, json ->
                gson.fromJson(json, Credentials::class.java)
            }
            .setBody(mapOf(
                "email" to username,
                "password" to password,
                "client_id" to API.key,
                "client_secret" to API.secret,
                "grant_type" to "password"
            ))

        return elloRequest
    }

    fun reauth(refreshToken: String): ElloRequest<Credentials> {
        val path = "/api/oauth/token"

        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path, requiresAnyToken = false, supportsAnonymousToken = true)
            .parser { gson, json ->
                gson.fromJson(json, Credentials::class.java)
            }
            .setBody(mapOf(
                "client_id" to API.key,
                "client_secret" to API.secret,
                "refresh_token" to refreshToken,
                "grant_type" to "refresh_token"
            ))

        return elloRequest
    }

    fun anonymousCreds(): ElloRequest<Credentials> {
        val path = "/api/oauth/token"

        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path, requiresAnyToken = false, supportsAnonymousToken = true)
            .parser { gson, json ->
                gson.fromJson(json, Credentials::class.java)
            }
            .setBody(mapOf(
                "client_id" to API.key,
                "client_secret" to API.secret,
                "grant_type" to "client_credentials"
            ))

        return elloRequest
    }
}
