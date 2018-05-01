package co.ello.android.ello


class PageParser<T> {
    val parser: ManyParser<T>
    val resultsKey: String

    constructor(resultsKey: String, parser: Parser) {
        this.parser = ManyParser(parser)
        this.resultsKey = resultsKey
    }

    fun parse(json: JSON): Pair<PageConfig, List<T>> {
        val objects = parser.parse(json = json[resultsKey])
        val next = json["next"].string
        val isLastPage = json["isLastPage"].boolean
        val config = PageConfig(next = next, isLastPage = isLastPage)
        return Pair(config, objects)
    }

}
