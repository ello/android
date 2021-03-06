package co.ello.android.ello


data class Love(
    val id: String,
    val isDeleted: Boolean,
    val postId: String,
    val userId: String
    ) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.LovesType)
    override fun update(property: Property, value: Any) {}

    val post: Post? get() = getLinkObject("post")
    val user: User? get() = getLinkObject("user")

    init {
        addLinkObject("post", postId, MappingType.PostsType)
        addLinkObject("user", userId, MappingType.UsersType)
    }
}
