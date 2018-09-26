package co.ello.android.ello

class CategoryUserParser : IdParser(table = MappingType.CategoryUsersType) {
    init {
        linkObject(MappingType.CategoriesType, "category")
        linkObject(MappingType.UsersType, "user")
        linkObject(MappingType.UsersType, "featuredBy")
        linkObject(MappingType.UsersType, "curatorBy")
        linkObject(MappingType.UsersType, "moderatorBy")
        linkObject(MappingType.PostsType, "post")
    }

    override fun parse(json: JSON): CategoryUser {
        val categoryUser = CategoryUser(
            id = json["id"].idValue,
            role = CategoryUser.Role.create(json["role"].stringValue.toLowerCase()) ?: CategoryUser.Role.Unspecified
        )

        categoryUser.mergeLinks(json["links"])

        return categoryUser
    }
}
