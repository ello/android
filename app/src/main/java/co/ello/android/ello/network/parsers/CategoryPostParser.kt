package co.ello.android.ello


class CategoryPostParser : IdParser(table = MappingType.CategoryPostsType) {
    init {
        linkObject(MappingType.CategoriesType, "submittedBy")
        linkObject(MappingType.UsersType, "submittedBy")
        linkObject(MappingType.UsersType, "featuredBy")
        linkObject(MappingType.UsersType, "unfeaturedBy")
        linkObject(MappingType.UsersType, "removedBy")
    }

    override fun parse(json: JSON): CategoryPost {
        val actions: MutableList<CategoryPost.Action> = mutableListOf()
        json["actions"].map?.let { actionsJson ->
            for ((name, actionJson) in actionsJson) {
                val action = CategoryPost.Action.create(name, json = actionJson) ?: continue
                actions.add(action)
            }
        }

        val submittedAt = json["submittedAt"].date ?: Globals.now
        val featuredAt = json["featuredAt"].date ?: Globals.now
        val unfeaturedAt = json["unfeaturedAt"].date ?: Globals.now
        val removedAt = json["removedAt"].date ?: Globals.now

        val categoryPost = CategoryPost(
            id = json["id"].stringValue,
            status = CategoryPost.Status.create(json["status"].stringValue) ?: CategoryPost.Status.Unspecified,
            actions = actions,
            submittedAt = submittedAt,
            featuredAt = featuredAt,
            unfeaturedAt = unfeaturedAt,
            removedAt = removedAt
        )

        categoryPost.mergeLinks(json["links"])

        return categoryPost
    }
}
