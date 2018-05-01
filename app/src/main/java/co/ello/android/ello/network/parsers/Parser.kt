package co.ello.android.ello


typealias Id = String
typealias Database = MutableMap<MappingType, MutableMap<String, JSON>>

open class Parser {

    data class Identifier(val id: Id, val table: MappingType)
    data class Link(val mappingType: MappingType, val jsonKey: String, val linkKey: String)

    private val linkedArrays: MutableList<Link> = mutableListOf()
    private val linkedObjects: MutableList<Link> = mutableListOf()

    companion object {
        fun saveToDB(parser: Parser, identifier: Identifier, db: Database): Model? {
            val table = db[identifier.table] ?: return null
            val json = table[identifier.id] ?: return null

            table.remove(identifier.id)
            db[identifier.table] = table
            val model = parser.parse(json) ?: return null
            Store.write { transaction ->
                transaction.setObject(model, key = identifier.id, collection = identifier.table)
            }
            return model
        }
    }

    fun linkArray(table: MappingType, _jsonKey: String? = null, _linkKey: String? = null) {
        val jsonKey = _jsonKey ?: table.pluralKey
        val linkKey = _linkKey ?: jsonKey.snakeCase
        linkedArrays.add(Link(mappingType = table, jsonKey = jsonKey, linkKey = linkKey))
    }

    fun linkObject(table: MappingType, _jsonKey: String? = null, _linkKey: String? = null) {
        val jsonKey = _jsonKey ?: table.singularKey
        val linkKey = _linkKey ?: jsonKey.snakeCase
        linkedObjects.add(Link(mappingType = table, jsonKey = jsonKey, linkKey = linkKey))
    }

    open fun identifier(json: JSON): Identifier? {
        return null
    }

    open fun flatten(json: JSON, identifier: Identifier, db: Database) {
        var foundJSON: JSON? = null
        db[identifier.table]?.let { existingTable ->
            val existing: JSON? = existingTable[identifier.id]
            existing?.let {
                for ((key, value) in json.mapValue) {
                    it[key] = value
                }
            }
            foundJSON = existing
        }
        val newJSON = foundJSON ?: json

        val links: MutableMap<String, JSON> = json["links"].map?.let { HashMap(it) } ?: mutableMapOf()
        for (link in linkedArrays) {
            val mappingType = link.mappingType
            val jsonKey = link.jsonKey
            val linkKey = link.linkKey
            val linkedObjects = json[jsonKey].list ?: continue
            val parser = mappingType.parser ?: continue

            val ids: MutableList<String> = mutableListOf()
            for (linkedJSON in linkedObjects) {
                val linkIdentifier = parser.identifier(linkedJSON) ?: continue

                parser.flatten(json = linkedJSON, identifier = linkIdentifier, db = db)
                ids.add(identifier.id)
            }
            val map: Map<String, Any> = mapOf("ids" to ids, "type" to mappingType.name)
            links[linkKey] = JSON(map)
        }

        for (link in linkedArrays) {
            val mappingType = link.mappingType
            val jsonKey = link.jsonKey
            val linkKey = link.linkKey
            val linkedJSON = json[jsonKey]
            val parser = mappingType.parser ?: continue
            val linkIdentifier = parser.identifier(linkedJSON) ?: continue

            parser.flatten(json = linkedJSON, identifier = linkIdentifier, db = db)
            val map: Map<String, Any> = mapOf("id" to linkIdentifier.id, "type" to mappingType.name)
            links[linkKey] = JSON(map)
        }

        newJSON["links"] = JSON(links)

        val table = db[identifier.table] ?: mutableMapOf()
        table[identifier.id] = newJSON
        db[identifier.table] = table
    }

    open fun parse(json: JSON): Model? {
        return null
    }
}
