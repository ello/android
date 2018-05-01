package co.ello.android.ello


class PostParser : IdParser(table = MappingType.postsType) {
    init {
        linkArray(MappingType.assetsType)
        linkArray(MappingType.categoriesType)

        linkObject(MappingType.usersType, "author")
        linkObject(MappingType.usersType, "repostAuthor")
        linkObject(MappingType.postsType, "repostedSource")
        linkObject(MappingType.artistInviteSubmissionsType)
    }

    override fun flatten(json: JSON, identifier: Identifier, db: Database) {
        val repostedSource = json["repostedSource"]
        identifier(json = repostedSource)?.let { repostIdentifier ->
            flatten(json = repostedSource, identifier = repostIdentifier, db = db)
            json["links"] = JSON(mapOf<String, Any>(
                "reposted_source" to mapOf<String, String>(
                    "id" to repostIdentifier.id,
                    "type" to MappingType.postsType.name
                )
            ))
        }

        super.flatten(json, identifier, db)
    }

    override fun parse(json: JSON): Post {
        val repostContent = RegionParser.graphQLRegions(json["repostContent"])
        val createdAt = json["createdAt"].stringValue.toDate() ?: Globals.now

        val post = Post(
            id = json["id"].stringValue,
            createdAt = createdAt,
            authorId = json["author"]["id"].stringValue,
            token = json["token"].stringValue,
            isAdultContent = false, // json["is_adult_content"].booleanValue,
            contentWarning = "", // json["content_warning"].stringValue,
            allowComments = true, // json["allow_comments"].booleanValue,
            isReposted = json["currentUserState"]["reposted"].boolean ?: false,
            isLoved = json["currentUserState"]["loved"].boolean ?: false,
            isWatching = json["currentUserState"]["watching"].boolean ?: false,
            summary = RegionParser.graphQLRegions(json["summary"]),
            content = RegionParser.graphQLRegions(json["content"], isRepostContent = repostContent.size > 0),
            body = RegionParser.graphQLRegions(json["body"], isRepostContent = repostContent.size > 0),
            repostContent = repostContent
        )

        post.artistInviteId = json["artistInviteSubmission"]["artistInvite"]["id"].id
        post.viewsCount = json["postStats"]["viewsCount"].int
        post.commentsCount = json["postStats"]["commentsCount"].int
        post.repostsCount = json["postStats"]["repostsCount"].int
        post.lovesCount = json["postStats"]["lovesCount"].int

        // post.links = json["links"].object

        return post
    }
}
