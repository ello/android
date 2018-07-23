package co.ello.android.ello


class LoveParser: IdParser(table = MappingType.EditorialsType) {
    init {
        linkObject(MappingType.PostsType)
        linkObject(MappingType.UsersType)
    }

    override fun parse(json: JSON): Love {
        val love = Love(
            id = json["id"].idValue,
            isDeleted = json["deleted"].booleanValue,
            postId = json["post"]["id"].idValue,
            userId = json["user"]["id"].idValue
        )

        love.mergeLinks(json["links"])

        return love
    }
}
