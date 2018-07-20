package co.ello.android.ello

import java.net.URL


data class User(
    val id: String,
    val username: String,
    val name: String,
    var relationshipPriority: RelationshipPriority,
    val postsAdultContent: Boolean,
    val viewsAdultContent: Boolean,
    val hasCommentingEnabled: Boolean,
    val hasSharingEnabled: Boolean,
    val hasRepostingEnabled: Boolean,
    val hasLovesEnabled: Boolean,
    val isCollaborateable: Boolean,
    val isHireable: Boolean
    ) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.UsersType)
    override fun update(property: Property, value: Any) = when (property) {
        Model.Property.relationshipPriority -> relationshipPriority = value as RelationshipPriority
        else -> {}
    }

    var avatar: Asset? = null
    var coverImage: Asset? = null

    var postsCount: Int? = null
    var lovesCount: Int? = null
    var followersCount: Int? = null
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
            val category: Category? = Store.read { transaction ->
                transaction.getObject(id, collection = MappingType.CategoriesType) as? Category
            }
            category?.let { listOf(it) } ?: emptyList()
        }

    private var _badges: List<Badge>? = null
    var badges: List<Badge>
        get() { return _badges ?: emptyList() }
        set(value) { _badges = value }
    var profile: Profile? = null

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

    fun coverImageURL(viewsAdultContent: Boolean? = false, animated: Boolean = false): URL? {
        if (animated && (!postsAdultContent || viewsAdultContent == true) && coverImage?.original?.url?.path?.endsWith(".gif") == true) {
            return coverImage?.original?.url
        }
        return coverImage?.oneColumnAttachment?.url
    }

    fun avatarURL(viewsAdultContent: Boolean? = false, animated: Boolean = false): URL? {
         if (animated && (!postsAdultContent || viewsAdultContent == true) && avatar?.original?.url?.path?.endsWith(".gif") == true) {
             return avatar?.original?.url
         }
        return avatar?.largeOrBest?.url
    }

}
