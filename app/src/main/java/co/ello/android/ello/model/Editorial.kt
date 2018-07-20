package co.ello.android.ello

import java.util.Date
import java.net.URL


data class Editorial(
    val id: String,
    val kind: Kind,
    val title: String,
    val subtitle: String?,
    val renderedSubtitle: String?,
    val url: URL?,
    val path: URL?
    ) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.EditorialsType)
    override fun update(property: Property, value: Any) {}

    data class JoinInfo(
        val email: String?,
        val username: String?,
        val password: String?,
        val submitted: Boolean
        )

    data class InviteInfo(
        val emails: String,
        val sent: Date?
        )

    enum class Kind(val value: String) {
        Post("post"),
        PostStream("post_stream"),
        External("external"),
        Internal("internal"),
        Invite("invite"),
        Join("join"),
        Unknown("unknown");

        companion object {
            fun create(value: String): Kind = when(value) {
                "POST" -> Kind.Post
                "POST_STREAM" -> Kind.PostStream
                "EXTERNAL" -> Kind.External
                "INTERNAL" -> Kind.Internal
                "INVITE" -> Kind.Invite
                "JOIN" -> Kind.Join
                else -> Unknown
            }
        }
    }

    enum class Size(val value: String) {
        Size1x1("oneByOneImage"),
        Size2x2("twoByTwoImage");

        companion object {
            val all: List<Size> = listOf(Size1x1, Size2x2)
        }
    }

    val postId: String? get() = post?.id
    val post: Post? get() = getLinkObject("post")

    var posts: List<Post>? = null
    val images: MutableMap<Size, Asset> = mutableMapOf()
}
