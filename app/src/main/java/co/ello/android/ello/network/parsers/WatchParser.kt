package co.ello.android.ello

class WatchParser : IdParser(table = MappingType.WatchesType){
    init {
        linkObject(MappingType.PostsType, "post")
        linkObject(MappingType.UsersType, "user")
    }
    override fun parse(json: JSON): Watch {
        val id = json["id"].idValue
        val createdAt = json["createdAt"].date ?: Globals.now
        val updatedAt = json["updatedAt"].date ?: Globals.now
        val postId = json["post"]["id"].idValue
        val userId = json["user"]["id"].idValue

        val watch = Watch(
                id = id,
                createdAt = createdAt,
                updatedAt = updatedAt,
                postId = postId,
                userId = userId,
                json = json["links"]
        )
        return watch
    }
}