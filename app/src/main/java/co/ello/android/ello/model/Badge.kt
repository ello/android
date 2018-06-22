package co.ello.android.ello

import java.net.URL


data class Badge(
    val slug: String,
    val name: String,
    val caption: String,
    val url: URL?,
    val imageURL: URL?
    ) : Model() {

    val isFeatured: Boolean get() = slug == "featured"
    var categories: List<Category>? = null

    companion object {
        var badges: Map<String, Badge> = emptyMap()

        fun userBadge(badge: Badge, categories: List<Category>?): Badge {
            val newBadge = Badge(
                slug = badge.slug,
                name = badge.name,
                caption = badge.caption,
                url = badge.url,
                imageURL = badge.imageURL
                )
            newBadge.categories = categories
            return newBadge
        }

        fun lookup(slug: String): Badge? {
            return badges[slug]
        }
    }
}


fun loadStaticBadges(queue: Queue) {
    val path = "/api/v2/badges.json"
    ElloRequest<List<Badge>>(ElloRequest.Method.GET, path, requiresAnyToken = false)
        .parser { json ->
            val parser = BadgeParser()
            json["badges"].listValue.map { parser.parse(it) }
        }
        .enqueue(queue)
        .onSuccess { badges ->
            println("=============== Badge.kt at line 51 ===============")
            println("badges: ${badges}")
            val badgeMap: MutableMap<String, Badge> = mutableMapOf()
            for (badge in badges) {
                badgeMap[badge.slug] = badge
            }
            Badge.badges = badgeMap
        }
}
