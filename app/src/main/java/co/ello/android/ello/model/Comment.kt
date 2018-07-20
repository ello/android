package co.ello.android.ello

import java.util.Date


data class Comment(
    val id: String,
    val createdAt: Date,
    val authorId: String,
    val postId: String,
    val content: List<Regionable>,
    val summary: List<Regionable>
    ) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.CommentsType)
    override fun update(property: Property, value: Any) {}

    val assets: List<Asset> get() = getLinkArray("assets")
    val author: User? get() = getLinkObject("author")
    val parentPost: Post? get() = getLinkObject("parent_post")
    val loadedFromPost: Post? get() = getLinkObject("loaded_from_post")
    var loadedFromPostId: String = postId
        set(value) {
            field = value
            addLinkObject("loaded_from_post", value, MappingType.PostsType)
        }

    init {
        addLinkObject("parent_post", postId, MappingType.PostsType)
        addLinkObject("loaded_from_post", postId, MappingType.PostsType)
        addLinkObject("author", authorId, MappingType.UsersType)
    }

    companion object {
        fun newCommentForPost(post: Post, currentUser: User): Comment {
            return Comment(
                id = UUIDString(),
                createdAt = Globals.now,
                authorId = currentUser.id,
                postId = post.id,
                content = emptyList(),
                summary = emptyList()
                )
        }
    }
}
