package co.ello.android.ello

import java.util.Date


data class Comment(
    val id: String,
    val createdAt: Date,
    val authorId: String,
    val postId: String,
    val content: List<Regionable>,
    val body: List<Regionable>,
    val summary: List<Regionable>
    ) : Model() {

    val assets: List<Asset> get() = getLinkArray("assets")
    val author: User? get() = getLinkObject("author")
    val parentPost: Post? get() = getLinkObject("parent_post")
    val loadedFromPost: Post? get() = getLinkObject("loaded_from_post")
    var loadedFromPostId: String = postId
        set(value: String) {
            field = value
            addLinkObject("loaded_from_post", value, MappingType.postsType)
        }

    init {
        addLinkObject("parent_post", postId, MappingType.postsType)
        addLinkObject("loaded_from_post", postId, MappingType.postsType)
        addLinkObject("author", authorId, MappingType.usersType)
    }

    companion object {
        fun newCommentForPost(post: Post, currentUser: User): Comment {
            return Comment(
                id = UUIDString(),
                createdAt = Globals.now,
                authorId = currentUser.id,
                postId = post.id,
                content = emptyList(),
                body = emptyList(),
                summary = emptyList()
                )
        }
    }
}
