package co.ello.android.ello


interface PostActionable {
    val postId: String
    val post: Post?
    val user: User?
}
