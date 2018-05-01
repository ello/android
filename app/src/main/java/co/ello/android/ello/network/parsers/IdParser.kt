package co.ello.android.ello

open class IdParser(val table: MappingType) : Parser() {

    override fun identifier(json: JSON): Parser.Identifier? {
        val id = json["id"].id ?: return null
        return Parser.Identifier(id = id, table = table)
    }
}
