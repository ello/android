package co.ello.android.ello


class CommentParser : IdParser(table = MappingType.CommentsType) {
    init {
        linkArray(MappingType.AssetsType)
        linkObject(MappingType.UsersType, "author")
        linkObject(MappingType.CategoriesType)
        linkObject(MappingType.ArtistInviteSubmissionsType)
    }

    override fun parse(json: JSON): Comment {
        val comment = Comment(
            id = json["id"].stringValue,
            createdAt = json["createdAt"].date ?: Globals.now,
            authorId = json["author_id"].stringValue,
            postId = json["post_id"].stringValue,
            content = RegionParser.graphQLRegions(json["content"]),
            body = RegionParser.graphQLRegions(json["body"]),
            summary = RegionParser.graphQLRegions(json["summary"])
            )

        comment.mergeLinks(json["links"])

        return comment
    }
}
