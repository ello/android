package co.ello.android.ello

import java.util.Date
import java.net.URL


data class Editorial(
    val id: String,
    val kind: Kind,
    val title: String,
    val subtitle: String?,
    val renderedSubtitle: String?,
    val postStreamURL: URL?,
    val url: URL?
    ) : Model() {

    data class JoinInfo(
        val email: String?,
        val username: String?,
        val password: String?,
        val submitted: Boolean)

    data class InviteInfo(
        val emails: String,
        val sent: Date?)

    enum class Kind(val value: String) {
        Post("post"),
        PostStream("post_stream"),
        External("external"),
        Internal("internal"),
        Invite("invite"),
        Join("join"),
        Unknown("unknown")
    }

    enum class Size(val value: String) {
        Size1x1("one_by_one_image"),
        // Size2x1("two_by_one_image"),
        // Size1x2("one_by_two_image"),
        Size2x2("two_by_two_image");

        companion object {
            val all: List<Size> = listOf(Size1x1, Size2x2)
        }
    }

    var join: JoinInfo? = null
    var invite: InviteInfo? = null

    val postId: String? get() = post?.id
    val post: Post? get() = getLinkObject("post")

    var posts: List<Post>? = null
    val images: MutableMap<Size, Asset> = mutableMapOf()

}
