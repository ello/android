package co.ello.android.ello

enum class CategoryLevel {
    Meta,
    Promoted,
    Primary,
    Secondary,
    Tertiary;

    val order: Int get() = ordinal

    companion object {
        fun create(value: String): CategoryLevel? = when(value) {
            "meta" -> Meta
            "promoted" -> Promoted
            "primary" -> Primary
            "secondary" -> Secondary
            "tertiary" -> Tertiary
            else -> null
        }
    }
}
