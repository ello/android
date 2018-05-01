package co.ello.android.ello


class ManyParser<T>(val parser: Parser) {
    class NotAnArray : Throwable()

    fun parse(json: JSON): List<T> {
        val objects = json.list ?: throw NotAnArray()

        val db: Database = mutableMapOf()
        val ids: MutableList<Parser.Identifier> = mutableListOf()
        for (objectJson in objects) {
            val identifier = parser.identifier(json = objectJson) ?: continue
            ids.add(identifier)
            parser.flatten(json = objectJson, identifier = identifier, db = db)
        }

        val many: List<T>?
        if (ids.size > 0) {
            many = ids.flatMap { identifier ->
                Parser.saveToDB(parser = parser, identifier = identifier, db = db)?.let {
                    @Suppress("UNCHECKED_CAST")
                    listOf(it as T)
                } ?: emptyList()
            }
        }
        else {
            many = null
        }

        for ((table, dbObjects) in db) {
            val tableParser = table.parser ?: continue

            for ((_, objectJson) in dbObjects) {
                val identifier = tableParser.identifier(json = objectJson) ?: continue
                Parser.saveToDB(parser = tableParser, identifier = identifier, db = db)
            }
        }

        if (many != null) {
            return many
        }
        else {
            return emptyList()
        }
    }
}
