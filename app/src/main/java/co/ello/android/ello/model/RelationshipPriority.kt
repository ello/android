package co.ello.android.ello


enum class RelationshipPriority(val value: String) {
    Following("friend"),
    Block("block"),
    Mute("mute"),
    Inactive("inactive"),
    None("none"),
    Null("null"),
    Me("self");

    val buttonName: String get() = when(this) {
        Following -> "following"
        else      -> this.value
    }

    val isMutedOrBlocked: Boolean get() = when(this) {
        Mute, Block -> true
        else        -> false
    }

    companion object {
        val all: List<RelationshipPriority> = listOf(
            RelationshipPriority.Following,
            RelationshipPriority.Block,
            RelationshipPriority.Mute,
            RelationshipPriority.Inactive,
            RelationshipPriority.None,
            RelationshipPriority.Null,
            RelationshipPriority.Me
            )

        fun create(value: String): RelationshipPriority? = when(value) {
            "friend"   -> RelationshipPriority.Following
            "block"    -> RelationshipPriority.Block
            "mute"     -> RelationshipPriority.Mute
            "inactive" -> RelationshipPriority.Inactive
            "none"     -> RelationshipPriority.None
            "null"     -> RelationshipPriority.Null
            "self"     -> RelationshipPriority.Me
            else       -> null
        }
    }
}
