package co.ello.android.ello

import java.util.Date


data class Watch(
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val postId: String,
    val userId: String
    ) : Model() {

    val post: Post? get() =  getLinkObject("post")
    val user: User? get() =  getLinkObject("user")

    init {
        addLinkObject("post", postId, MappingType.PostsType)
        addLinkObject("user", userId, MappingType.UsersType)
    }
}
