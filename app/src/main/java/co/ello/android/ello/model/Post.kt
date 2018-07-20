package co.ello.android.ello

import java.util.Date
import java.net.URL


data class Post(
    val id: String,
    val createdAt: Date,
    val authorId: String,
    val token: String,
    val isAdultContent: Boolean,
    val contentWarning: String,
    val allowComments: Boolean,
    val isReposted: Boolean,
    val isLoved: Boolean,
    val isWatching: Boolean,
    val summary: List<Regionable>,
    val content: List<Regionable>,
    val body: List<Regionable>,
    val repostContent: List<Regionable>
    ) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.PostsType)
    override fun update(property: Property, value: Any) {}

    var artistInviteId: String? = null
    var viewsCount: Int? = null
    var commentsCount: Int? = null
    var repostsCount: Int? = null
    var lovesCount: Int? = null

    val assets: List<Asset> get() = getLinkArray("assets")
    val firstImageURL: URL? get() = assets.firstOrNull()?.largeOrBest?.url
    val author: User? get() = getLinkObject("author")
    val categoryPosts: List<CategoryPost> get() = getLinkArray("category_posts")
    val category: Category? get() = categoryPosts.firstOrNull()?.category ?: getLinkObject("category")
    val repostAuthor: User? get() = repostSource?.author ?: getLinkObject("repost_author")
    val repostSource: Post? get() = getLinkObject("reposted_source")
    val featuredBy: User? get() = categoryPosts.firstOrNull()?.featuredBy

    val comments: List<Comment>? get() {
        val nestedComments: List<Comment> = getLinkArray("comments")
        for (comment in nestedComments) {
            comment.loadedFromPostId = this.id
        }
        return nestedComments
    }
    val shareLink: String? get() = author?.let { "${Globals.baseURL}/${it.username}/post/${token}" }
    val isCollapsed: Boolean get() = !contentWarning.isEmpty()
    val isRepost: Boolean get() = repostContent.size > 0
    val notificationContent: List<Regionable>? get() =
        if (isRepost) repostContent
        else content

    init {
        addLinkObject("author", authorId, MappingType.UsersType)
    }

    fun contentFor(gridView: Boolean): List<Regionable>? =
        if (gridView) summary
        else content
}
