package co.ello.android.ello

import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch
import java.net.URL


data class Badge(
    val slug: String,
    val name: String,
    val caption: String,
    val url: URL?,
    val imageURL: URL?
    ) : Model() {

    override val identifier = null
    override fun update(property: Property, value: Any) {}

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
    val request = ElloRequest<List<Badge>>(ElloRequest.Method.GET, path, requiresAnyToken = false)
        .parser { json ->
            val parser = BadgeParser()
            json["badges"].listValue.map { parser.parse(it) }
        }
    launch(UI) {
        val result = request.enqueue(queue)
        when (result) {
            is Success -> {
                val badges = result.value
                val badgeMap: MutableMap<String, Badge> = mutableMapOf()
                for (badge in badges) {
                    badgeMap[badge.slug] = badge
                }
                Badge.badges = badgeMap
            }
            is Failure -> println("load badges error: ${result.error}")
        }
    }
}
