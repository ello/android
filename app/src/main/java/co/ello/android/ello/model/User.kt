package co.ello.android.ello


data class User(
    val id: String,
    val username: String,
    val name: String,
    val relationshipPriority: RelationshipPriority,
    val postsAdultContent: Boolean,
    val viewsAdultContent: Boolean,
    val hasCommentingEnabled: Boolean,
    val hasSharingEnabled: Boolean,
    val hasRepostingEnabled: Boolean,
    val hasLovesEnabled: Boolean,
    val isCollaborateable: Boolean,
    val isHireable: Boolean
    ) : Model() {

    var avatar: Asset? = null
    var coverImage: Asset? = null

    var postsCount: Int? = null
    var lovesCount: Int? = null
    var followersCount: String? = null // string due to this returning "∞" for the ello user
    var followingCount: Int? = null

    var formattedShortBio: String? = null
    var externalLinksList: List<ExternalLink>? = null
    val externalLinksString: String? get() {
        val externalLinksList = this.externalLinksList ?: return null
        val urls = externalLinksList.map { it.url.toExternalForm() }
        return urls.joinToString(", ")
    }

    var onboardingVersion: Int? = null
    var totalViewsCount: Int? = null
    var location: String? = null
    val hasProfileData: Boolean get() = postsCount != null && lovesCount != null && followingCount != null && followersCount != null && totalViewsCount != null

    val categories: List<Category>? get() = getLinkArray("categories")
    val hasSubscribedCategory: Boolean get() = followedCategoryIds.size > 0
    var followedCategoryIds: Set<String> = emptySet()
    val followedCategories: List<Category> get() = followedCategoryIds.flatMap { id ->
            var category: Category? = null
            TODO("ElloLinkedStore")
            // ElloLinkedStore.shared.readConnection.read { transaction in
            //     category = transaction.object(forKey: id, inCollection: "categories") as? Category
            // }
            category?.let { listOf(it) } ?: emptyList()
        }

    private var _badges: List<Badge>? = null
    var badges: List<Badge>
        get() { return _badges ?: emptyList() }
        set(value: List<Badge>) { _badges = value }
    var profile: Profile? = null

    val isCurrentUser: Boolean get() { return profile != null }
    val atName: String get() = "@${this.username}"
    val displayName: String get() = if (name.isEmpty()) atName else name
    val shareLink: String get() = "${Globals.baseURL}/${username}"
    val isFeatured: Boolean get() = (categories?.size ?: 0) > 0
    val formattedTotalCount: String? get() {
        val count = totalViewsCount ?: return null

        if (count < 1000) {
            return "<1K"
        }
        return count.numberToHuman(rounding = 1, showZero = true)
    }

}
