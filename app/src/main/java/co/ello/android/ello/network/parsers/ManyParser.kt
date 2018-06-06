package co.ello.android.ello


class ManyParser<T: Model>(val parser: Parser) {
    object NotAnArray : Throwable()

    fun parse(json: JSON): List<T> {
        val objects = json.list ?: throw NotAnArray

        val db: Database = mutableMapOf()
        val ids: MutableList<Parser.Identifier> = mutableListOf()
        for (objectJson in objects) {
            val identifier = parser.identifier(json = objectJson) ?: continue
            ids.add(identifier)
            parser.flatten(json = objectJson, identifier = identifier, db = db)
        }

        for ((table, dbObjects) in db) {
            val tableParser = table.parser ?: continue

            for ((_, objectJson) in dbObjects) {
                val identifier = tableParser.identifier(json = objectJson) ?: continue
                Parser.saveToDB(parser = tableParser, identifier = identifier, db = db)
            }
        }

        return ids.flatMap { identifier ->
            val id = identifier.id
            val mappingType = identifier.table
            val model: T? = Store.read { transaction ->
                @Suppress("UNCHECKED_CAST")
                transaction.getObject(id, collection = mappingType) as? T
            }
            model?.let { listOf(it) } ?: emptyList()
        }
    }
}
