package co.ello.android.ello

class WatchParser : IdParser(table = MappingType.WatchesType){
    override fun parse(json: JSON): Model? {
        val id = json["id"].idValue
        val createdAt = json["createdAt"].date
        val updatedAt = json["updatedAt"].date
        val postId = json["post"]["id"].idValue
        val userId = json["author"]["id"].idValue

        val watch = Watch(
                id = id,
                createdAt = createdAt,
                updatedAt = updatedAt,
                postId = postId,
                userId = userId
        )
        return watch
    }
}