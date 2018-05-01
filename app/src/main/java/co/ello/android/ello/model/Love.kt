package co.ello.android.ello

import java.util.Date


data class Love(
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val isDeleted: Boolean,
    val postId: String,
    val userId: String
    ) : Model() {

    val post: Post? get() = getLinkObject("post")
    val user: User? get() = getLinkObject("user")

    init {
        addLinkObject("post", postId, MappingType.postsType)
        addLinkObject("user", userId, MappingType.usersType)
    }
}
