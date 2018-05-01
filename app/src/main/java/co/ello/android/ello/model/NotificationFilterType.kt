package co.ello.android.ello


enum class NotificationFilterType {
    All,
    Comments,
    Mention,
    Heart,
    Repost,
    Relationship;

    val category: String? get() = when(this) {
        Comments -> "comments"
        Mention -> "mentions"
        Heart -> "loves"
        Repost -> "reposts"
        Relationship -> "relationships"
        All -> null
    }

    companion object {
        fun create(value: String): NotificationFilterType = when(value) {
            "comments" -> Comments
            "mentions" -> Mention
            "loves" -> Heart
            "reposts" -> Repost
            "relationships" -> Relationship
            else -> All
        }
    }
}
