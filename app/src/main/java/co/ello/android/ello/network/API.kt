package co.ello.android.ello

class API {
    companion object {
        val ENVIRONMENTS = arrayOf("Production", "Ninja", "Stage 1", "Stage 2", "Rainbow")
        var key: String = ""
        var secret: String = ""
        var domain: String = ""

        fun init(environment: String?) {
            val (key, secret, domain) = when (environment) {
                "Production" -> Triple(BuildConfig.PROD_CLIENT_KEY, BuildConfig.PROD_CLIENT_SECRET, BuildConfig.PROD_DOMAIN)
                "Ninja" -> Triple(BuildConfig.NINJA_CLIENT_KEY, BuildConfig.NINJA_CLIENT_SECRET, BuildConfig.NINJA_DOMAIN)
                "Stage 1" -> Triple(BuildConfig.STAGE1_CLIENT_KEY, BuildConfig.STAGE1_CLIENT_SECRET, BuildConfig.STAGE1_DOMAIN)
                "Stage 2" -> Triple(BuildConfig.STAGE2_CLIENT_KEY, BuildConfig.STAGE2_CLIENT_SECRET, BuildConfig.STAGE2_DOMAIN)
                "Rainbow" -> Triple(BuildConfig.RAINBOW_CLIENT_KEY, BuildConfig.RAINBOW_CLIENT_SECRET, BuildConfig.RAINBOW_DOMAIN)
                else -> return
            }
            this.key = key
            this.secret = secret
            this.domain = domain
        }
    }

    enum class StreamFilter(val value: String) {
        Featured("FEATURED"),
        Trending("TRENDING"),
        Recent("RECENT"),
        Shop("SHOP");

        companion object {
            fun create(value: String): StreamFilter? = when(value) {
                "FEATURED" -> StreamFilter.Featured
                "TRENDING" -> StreamFilter.Trending
                "RECENT" -> StreamFilter.Recent
                "SHOP" -> StreamFilter.Shop
                else -> null
            }
        }
    }

    enum class NotificationFilter(val value: String) {
        All("ALL"),
        Comments("COMMENTS"),
        Loves("LOVES"),
        Mentions("MENTIONS"),
        Relationships("RELATIONSHIPS"),
        Reposts("REPOSTS");

        companion object {
            fun create(value: String): NotificationFilter? = when (value) {
                "ALL" -> NotificationFilter.All
                "COMMENTS" -> NotificationFilter.Comments
                "LOVES" -> NotificationFilter.Loves
                "MENTIONS" -> NotificationFilter.Mentions
                "RELATIONSHIPS" -> NotificationFilter.Relationships
                "REPOSTS" -> NotificationFilter.Reposts
                else -> null
            }
        }
    }

    fun followingPostStream(filter: StreamFilter = StreamFilter.Recent,before: String? = null): GraphQLRequest<Pair<PageConfig, List<Post>>> {
        return GraphQLRequest<Pair<PageConfig, List<Post>>>("followingPostStream")
                .parser { PageParser<Post>("posts", PostParser()).parse(it) }
                .setVariables(
                        GraphQLRequest.Variable.enum("kind", filter.value, "StreamKind"),
                        GraphQLRequest.Variable.optionalString("before", before)
                )
                .setBody(Fragments.postStreamBody)
    }

    fun globalPostStream(filter: StreamFilter, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Post>>> {
        return GraphQLRequest<Pair<PageConfig, List<Post>>>("globalPostStream")
            .parser { PageParser<Post>("posts", PostParser()).parse(it) }
            .setVariables(
                GraphQLRequest.Variable.enum("kind", filter.value, "StreamKind"),
                GraphQLRequest.Variable.optionalString("before", before)
            )
            .setBody(Fragments.postStreamBody)
    }

    fun subscribedPostStream(filter: StreamFilter, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Post>>> {
        return GraphQLRequest<Pair<PageConfig, List<Post>>>("subscribedPostStream")
            .parser { PageParser<Post>("posts", PostParser()).parse(it) }
            .setVariables(
                GraphQLRequest.Variable.enum("kind", filter.value, "StreamKind"),
                GraphQLRequest.Variable.optionalString("before", before)
            )
            .setBody(Fragments.postStreamBody)
    }

    fun categoryPostStream(filter: StreamFilter, category: Token, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Post>>> {
        val categoryVar: GraphQLRequest.Variable = when(category) {
            is ID -> GraphQLRequest.Variable.optionalInt("id", category.id.toInt())
            is Slug -> GraphQLRequest.Variable.optionalInt("slug", category.slug.toInt())
        }

        return GraphQLRequest<Pair<PageConfig, List<Post>>>("categoryPostStream")
                .parser { PageParser<Post>("posts", PostParser()).parse(it) }
                .setVariables(
                        GraphQLRequest.Variable.enum("kind", filter.value, "StreamKind"),
                        categoryVar,
                        GraphQLRequest.Variable.optionalString("before", before)
                )
                .setBody(Fragments.postStreamBody)
    }

    fun subscribedCategories(): GraphQLRequest<List<Category>> {
        return GraphQLRequest<List<Category>>("categoryNav")
            .parser { ManyParser<Category>(CategoryParser()).parse(it) }
            .setBody(Fragments.categoriesBody)
    }

    fun editorialStream(before: String? = null): GraphQLRequest<Pair<PageConfig, List<Editorial>>> {
        return GraphQLRequest<Pair<PageConfig, List<Editorial>>>("editorialStream")
            .parser { PageParser<Editorial>("editorials", EditorialParser()).parse(it) }
            .setVariables(
                GraphQLRequest.Variable.optionalString("before", before),
                GraphQLRequest.Variable.optionalBoolean("preview", false),
                GraphQLRequest.Variable.optionalInt("perPage", null)
            )
            .setBody(Fragments.editorialsBody)
    }

    fun notificationsStream(category: NotificationFilter, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Notification>>> {
        return GraphQLRequest<Pair<PageConfig, List<Notification>>>("notificationStream")
                .parser { PageParser<Notification>("notifications", NotificationParser()).parse(it) }
                .setVariables(
                        GraphQLRequest.Variable.optionalString("before", before),
                        GraphQLRequest.Variable.enum("category", category.value, "NotificationCategory"),
                        GraphQLRequest.Variable.optionalInt("perPage", null)
                )
                .setBody(NotificationFragment.notificationStreamBody)
    }

    fun postDetail(token: Token, username: String?): GraphQLRequest<Post> {
        return GraphQLRequest<Post>("post")
            .parser { OneParser<Post>(PostParser()).parse(it) }
            .setVariables(
                token.variable,
                GraphQLRequest.Variable.optionalString("username", username)
            )
            .setBody(Fragments.postBody)
    }

    fun postComments(token: Token, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Comment>>> {
        return GraphQLRequest<Pair<PageConfig, List<Comment>>>("commentStream")
            .parser { PageParser<Comment>("comments", CommentParser()).parse(it) }
            .setVariables(
                token.variable,
                GraphQLRequest.Variable.optionalString("before", before)
            )
            .setBody(Fragments.commentStreamBody)
    }

    fun userDetail(token: Token): GraphQLRequest<User> {
        return GraphQLRequest<User>("findUser")
            .parser { OneParser<User>(UserParser()).parse(it) }
            .setVariables(
                token.variable
            )
            .setBody(Fragments.userBody)
    }

    fun userPosts(username: String, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Post>>> {
        return GraphQLRequest<Pair<PageConfig, List<Post>>>("userPostStream")
            .parser { PageParser<Post>("posts", PostParser()).parse(it) }
            .setVariables(
                GraphQLRequest.Variable.string("username", username),
                GraphQLRequest.Variable.optionalString("before", before)
            )
            .setBody(Fragments.postStreamBody)
    }

    fun userLoves(username: String, before: String? = null): GraphQLRequest<Pair<PageConfig, List<Love>>> {
        return GraphQLRequest<Pair<PageConfig, List<Love>>>("userLoveStream")
            .parser { PageParser<Love>("loves", LoveParser()).parse(it) }
            .setVariables(
                GraphQLRequest.Variable.string("username", username),
                GraphQLRequest.Variable.optionalString("before", before)
            )
            .setBody(Fragments.loveStreamBody)
    }

    fun updateRelationship(userId: String, relationship: RelationshipPriority): ElloRequest<Relationship> {
        val path = "/api/v2/users/$userId/add/${relationship.value}"
        val elloRequest = ElloRequest<Relationship>(ElloRequest.Method.POST, path)
                .parser { json ->
                    Relationship(
                        id = json["id"].idValue,
                        ownerId = json["links"]["owner"]["id"].idValue,
                        subjectId = json["links"]["subject"]["id"].idValue
                        )
                }

        return elloRequest
    }

    fun join(email: String, username: String, password: String): ElloRequest<Credentials> {
        val path = "/api/v2/join"
        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path)
                .parser { CredentialsParser(isAnonymous = false).parse(it) }
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
        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path, requiresAnyToken = false, shouldRetryAuth = false)
            .parser { CredentialsParser(isAnonymous = false).parse(it) }
            .setBody(mapOf(
                "email" to username,
                "password" to password,
                "client_id" to API.key,
                "client_secret" to API.secret,
                "grant_type" to "password"
            ))

        return elloRequest
    }

    fun reauth(refreshToken: String, isAnonymous: Boolean): ElloRequest<Credentials> {
        val path = "/api/oauth/token"
        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path, requiresAnyToken = false, shouldRetryAuth = false)
            .parser { CredentialsParser(isAnonymous = isAnonymous).parse(it) }
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
        val elloRequest = ElloRequest<Credentials>(ElloRequest.Method.POST, path, requiresAnyToken = false, shouldRetryAuth = false)
            .parser { CredentialsParser(isAnonymous = true).parse(it) }
            .setBody(mapOf(
                "client_id" to API.key,
                "client_secret" to API.secret,
                "grant_type" to "client_credentials"
            ))

        return elloRequest
    }
}
