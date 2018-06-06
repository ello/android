package co.ello.android.ello


class OneParser<T: Model>(val parser: Parser) {
    object NotIdentifiable : Throwable()
    object WrongType : Throwable()

    fun parse(json: JSON): T {
        val identifier = parser.identifier(json) ?: throw NotIdentifiable

        val db: Database = mutableMapOf()
        parser.flatten(json = json, identifier = identifier, db = db)
        val one = Parser.saveToDB(parser = parser, identifier = identifier, db = db)

        for ((table, dbObjects) in db) {
            val tableParser = table.parser ?: continue

            for ((_, objectJson) in dbObjects) {
                val identifier = tableParser.identifier(json = objectJson) ?: continue
                Parser.saveToDB(parser = tableParser, identifier = identifier, db = db)
            }
        }

        @Suppress("UNCHECKED_CAST")
        return one as? T ?: throw WrongType
    }
}
