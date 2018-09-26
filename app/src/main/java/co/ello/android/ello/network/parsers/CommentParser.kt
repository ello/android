package co.ello.android.ello


class CommentParser : IdParser(table = MappingType.CommentsType) {
    init {
        linkArray(MappingType.AssetsType)
        linkObject(MappingType.UsersType, "author")
        linkObject(MappingType.PostsType)
    }

    override fun parse(json: JSON): Comment {
        val comment = Comment(
            id = json["id"].idValue,
            createdAt = json["createdAt"].date ?: Globals.now,
            authorId = json["author"]["id"].idValue,
            postId = json["parentPost"]["id"].idValue,
            content = RegionParser.graphQLRegions(json["content"]),
            summary = RegionParser.graphQLRegions(json["summary"])
            )

        comment.mergeLinks(json["links"])

        return comment
    }
}
