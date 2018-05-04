package co.ello.android.ello

import java.util.Date


data class CategoryPost(
    val id: String,
    val status: CategoryPost.Status,
    var actions: List<CategoryPost.Action>,
    val submittedAt: Date,
    val featuredAt: Date,
    val unfeaturedAt: Date,
    val removedAt: Date
    ) : Model() {

    val category: Category? get() { return getLinkObject("category") }
    val submittedBy: User? get() { return getLinkObject("submittedBy") }
    val featuredBy: User? get() { return getLinkObject("featuredBy") }
    val unfeaturedBy: User? get() { return getLinkObject("unfeaturedBy") }
    val removedBy: User? get() { return getLinkObject("removedBy") }

    enum class Status(val value: String) {
        Featured("featured"),
        Submitted("submitted"),
        Unspecified("unspecified");

        companion object {
            fun create(value: String): Status? = when(value) {
                "featured" -> Status.Featured
                "submitted" -> Status.Submitted
                "unspecified" -> Status.Unspecified
                else -> null
            }
        }
    }

    data class Action(
        val name: Name,
        val label: String,
        val request: ElloRequest<CategoryPost>
        ) {

        enum class Name(val value: String) {
            feature("feature"),
            unfeature("unfeature");

            companion object {
                fun create(value: String): Name? = when(value) {
                    "feature" -> Name.feature
                    "unfeature" -> Name.unfeature
                    else -> null
                }
            }
        }

        companion object {
            fun create(nameStr: String, json: JSON): Action? {
                val method = json["method"].string?.toRequestMethod ?: return null
                val path = json["href"].string ?: return null
                val label = json["label"].string ?: return null
                val parameters = json["body"].obj ?: return null
                val name = Name.create(nameStr) ?: return null
                return Action(name = name, label = label, request = ElloRequest<CategoryPost>(method = method, path = path, parameters = parameters, requiresAnyToken = true, supportsAnonymousToken = false))
            }
        }
    }

    fun hasAction(name: Action.Name): Boolean {
        return actions.any { it.name == name }
    }
}
