package co.ello.android.ello


class PostParser : IdParser(table = MappingType.PostsType) {
    init {
        linkArray(MappingType.AssetsType)
        linkArray(MappingType.CategoriesType)

        linkObject(MappingType.UsersType, "author")
        linkObject(MappingType.UsersType, "repostAuthor")
        linkObject(MappingType.PostsType, "repostedSource")
        linkObject(MappingType.ArtistInviteSubmissionsType)
    }

    override fun flatten(json: JSON, identifier: Identifier, db: Database) {
        val repostedSource = json["repostedSource"]
        identifier(json = repostedSource)?.let { repostIdentifier ->
            flatten(json = repostedSource, identifier = repostIdentifier, db = db)
            json["links"] = JSON(mapOf<String, Any>(
                "reposted_source" to mapOf<String, String>(
                    "id" to repostIdentifier.id,
                    "type" to MappingType.PostsType.name
                )
            ))
        }

        super.flatten(json, identifier, db)
    }

    override fun parse(json: JSON): Post {
        val repostContent = RegionParser.graphQLRegions(json["repostContent"])

        val post = Post(
            id = json["id"].stringValue,
            createdAt = json["createdAt"].date ?: Globals.now,
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

        post.mergeLinks(json["links"])

        return post
    }
}
