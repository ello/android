package co.ello.android.ello

import java.util.Date

data class Watch(
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val postId: String,
    val userId: String,
    val json: JSON
    ) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.WatchesType)
    override fun update(property: Property, value: Any) {}

    val post: Post? get() =  getLinkObject("post")
    val user: User? get() =  getLinkObject("user")

    init {
        val jsonMap = json.map ?: emptyMap()
        for ((key, json) in jsonMap) {
            println(json)
            addLinkObject(key, json["id"].idValue, MappingType.create(json["type"].stringValue) as MappingType)
        }
    }
}
