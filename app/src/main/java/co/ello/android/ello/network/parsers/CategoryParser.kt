package co.ello.android.ello


class CategoryParser : IdParser(table = MappingType.CategoriesType) {
    override fun parse(json: JSON): Category {
        val level = CategoryLevel.create(json["level"].stringValue) ?: CategoryLevel.Tertiary

        val category = Category(
            id = json["id"].stringValue,
            name = json["name"].stringValue,
            slug = json["slug"].stringValue,
            order = json["order"].intValue,
            allowInOnboarding = json["allowInOnboarding"].boolean ?: true,
            isCreatorType = json["isCreatorType"].boolean ?: true,
            level = level,
            tileImage = Attachment.fromJSON(json["tileImage"]["large"])
            )

        category.mergeLinks(json["links"])

        return category
    }
}