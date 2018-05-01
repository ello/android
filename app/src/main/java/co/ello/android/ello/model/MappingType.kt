package co.ello.android.ello

sealed class MappingType {
    val name: String
    val pluralKey: String
    val singularKey: String
    private val parserGenerator: (() -> Parser)?
    val parser: Parser? get() = parserGenerator?.invoke()

    constructor(name: String, singularKey: String, pluralKey: String? = null, parser: (() -> Parser)? = null) {
        this.name = name
        this.pluralKey = pluralKey ?: name
        this.singularKey = singularKey
        this.parserGenerator = parser
    }

    object activitiesType : MappingType(name = "activities", singularKey = "activity")
    object amazonCredentialsType : MappingType(name = "credentials", singularKey = "credentials")
    object announcementsType : MappingType(name = "announcements", singularKey = "announcement")
    object artistInvitesType : MappingType(name = "artist_invites", singularKey = "artist_invite")
    object artistInviteSubmissionsType : MappingType(name = "artist_invite_submissions", singularKey = "artist_invite_submission")
    object assetsType : MappingType(name = "assets", singularKey = "asset")
    object autoCompleteResultType : MappingType(name = "autocomplete_results", singularKey = "autocomplete_result")
    object availabilityType : MappingType(name = "availability", singularKey = "availability", pluralKey = "availabilities")
    object categoriesType : MappingType(name = "categories", singularKey = "category")
    object categoryPostsType : MappingType(name = "category_posts", singularKey = "category_post")
    object commentsType : MappingType(name = "comments", singularKey = "comment")
    object dynamicSettingsType : MappingType(name = "settings", singularKey = "setting")
    object editorials : MappingType(name = "editorials", singularKey = "editorial")
    object errorsType : MappingType(name = "errors", singularKey = "error")
    object errorType : MappingType(name = "error", singularKey = "error", pluralKey = "errors")
    object lovesType : MappingType(name = "loves", singularKey = "love")
    object noContentType : MappingType(name = "204", singularKey = "204")
    object pageHeadersType : MappingType(name = "page_headers", singularKey = "page_header")
    object postsType : MappingType(name = "posts", singularKey = "post", parser = { PostParser() })
    object profilesType : MappingType(name = "profiles", singularKey = "profile")
    object relationshipsType : MappingType(name = "relationships", singularKey = "relationship")
    object usernamesType : MappingType(name = "usernames", singularKey = "username")
    object usersType : MappingType(name = "users", singularKey = "user")
    object watchesType : MappingType(name = "watches", singularKey = "watch")

    companion object {
        fun create(value: String): MappingType? = when (value) {
            "activities"                -> MappingType.activitiesType
            "credentials"               -> MappingType.amazonCredentialsType
            "announcements"             -> MappingType.announcementsType
            "artist_invites"            -> MappingType.artistInvitesType
            "artist_invite_submissions" -> MappingType.artistInviteSubmissionsType
            "assets"                    -> MappingType.assetsType
            "autocomplete_results"      -> MappingType.autoCompleteResultType
            "availability"              -> MappingType.availabilityType
            "categories"                -> MappingType.categoriesType
            "category_posts"            -> MappingType.categoryPostsType
            "comments"                  -> MappingType.commentsType
            "settings"                  -> MappingType.dynamicSettingsType
            "editorials"                -> MappingType.editorials
            "errors"                    -> MappingType.errorsType
            "error"                     -> MappingType.errorType
            "loves"                     -> MappingType.lovesType
            "204"                       -> MappingType.noContentType
            "page_headers"              -> MappingType.pageHeadersType
            "posts"                     -> MappingType.postsType
            "profiles"                  -> MappingType.profilesType
            "relationships"             -> MappingType.relationshipsType
            "usernames"                 -> MappingType.usernamesType
            "users"                     -> MappingType.usersType
            "watches"                   -> MappingType.watchesType
            else -> null
        }
    }
}
