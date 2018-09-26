package co.ello.android.ello

data class CategoryUser(
        val id: String,
        val role: Role
) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.CategoryUsersType)
    override fun update(property: Property, value: Any) {}

    val category: Category? get() { return getLinkObject("category") }
    val user: User? get() { return getLinkObject("user") }
    val featuredBy: User? get() { return getLinkObject("featured_by") }
    val curatorBy: User? get() { return getLinkObject("curator_by") }
    val moderatorBy: User? get() { return getLinkObject("moderator_by") }

    enum class Role(val value: String) {
        Featured("featured"),
        Curator("curator"),
        Moderator("moderator"),
        Unspecified("unspecified");

        companion object {
            fun create(value: String): CategoryUser.Role? = when(value) {
                "featured" -> CategoryUser.Role.Featured
                "curator" -> CategoryUser.Role.Curator
                "moderator" -> CategoryUser.Role.Moderator
                "unspecified" -> CategoryUser.Role.Unspecified
                else -> null
            }
        }
    }
}