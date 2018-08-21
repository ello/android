package co.ello.android.ello


open class Model {
    sealed class Link {
        data class One(val id: String, val type: MappingType) : Link()
        data class Many(val ids: List<String>, val type: MappingType) : Link()

        companion object {}
    }

    var links: MutableMap<String, Model.Link> = mutableMapOf()

    inline fun <reified T: Model> getLinkObject(key: String): T? {
        val link = links[key] ?: return null
        val (id, mappingType) = when (link) {
                is Link.One -> { Pair(link.id, link.type) }
                else -> null
            } ?: return null

        var obj: T? = Store.read { transaction ->
            transaction.getObject(key = id, collection = mappingType) as? T
        }
        return obj
    }

    inline fun <reified T: Model> getLinkArray(key: String): List<T> {
        val link = links[key] ?: return emptyList()
        val (ids, mappingType) = when (link) {
                is Link.Many -> { Pair(link.ids, link.type) }
                else -> null
            } ?: return emptyList()

        var objects: List<T> = ids.flatMap { id ->
            val model: T? = Store.read { transaction ->
                transaction.getObject(id, collection = mappingType) as? T
            }
            model?.let { listOf(it) } ?: emptyList()
        }
        return objects
    }

    fun mergeLinks(links: JSON) {
        val linksMap = links.map ?: return
        for ((key, json) in linksMap) {
            val link = Link.decode(key, json) ?: continue
            this.links[key] = link
        }
    }

    fun addLinkObject(key: String, id: String, type: MappingType) {
        links[key] = Link.One(id, type)
    }

    fun storeLinkObject(model: Model, key: String, id: String, type: MappingType) {
        addLinkObject(key, id, type)
        Store.write { transaction ->
            transaction.setObject(model, id, collection = type)
        }
    }

    fun addLinkArray(key: String, array: List<String>, type: MappingType) {
        links[key] = Link.Many(array, type)
    }

}

fun Model.Link.encode(): Map<String, Any> {
    return when (this) {
        is Model.Link.One -> mapOf("id" to this.id, "type" to this.type.name)
        is Model.Link.Many -> mapOf("ids" to this.ids, "type" to this.type.name)
    }
}

fun Model.Link.Companion.decode(key: String, json: JSON): Model.Link? {
    val string = json.string
    val list = json.list
    if (string != null) {
        return decode(key, JSON(mapOf("id" to string, "type" to key)))
    }
    else if (list != null) {
        return decode(key, JSON(mapOf("ids" to list.map { it.stringValue }, "type" to key)))
    }

    val typeStr = json["type"].string ?: return null
    val type = MappingType.create(typeStr) ?: return null
    val id = json["id"].string
    val ids  = json["ids"].list

    if (id != null) {
        return Model.Link.One(id, type)
    }
    else if (ids != null) {
        return Model.Link.Many(ids.map { it.stringValue }, type)
    }
    else {
        return null
    }
}
