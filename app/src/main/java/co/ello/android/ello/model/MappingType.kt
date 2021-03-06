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

    object ActivitiesType : MappingType(name = "activities", singularKey = "activity")
    object AnnouncementsType : MappingType(name = "announcements", singularKey = "announcement")
    object ArtistInvitesType : MappingType(name = "artist_invites", singularKey = "artistInvite")
    object ArtistInviteSubmissionsType : MappingType(name = "artist_invite_submissions", singularKey = "artistInviteSubmission")
    object AssetsType : MappingType(name = "assets", singularKey = "asset", parser = { AssetParser() })
    object AttachmentsType : MappingType(name = "attachments", singularKey = "attachment")
    object AutoCompleteResultType : MappingType(name = "autocomplete_results", singularKey = "autocompleteResult")
    object AvailabilityType : MappingType(name = "availability", singularKey = "availability", pluralKey = "availabilities")
    object CategoriesType : MappingType(name = "categories", singularKey = "category", parser = { CategoryParser() })
    object CategoryPostsType : MappingType(name = "category_posts", singularKey = "categoryPost", parser = { CategoryPostParser() })
    object CategoryUsersType : MappingType(name = "category_users", singularKey = "categoryUser", parser = { CategoryUserParser() })
    object CredentialsType : MappingType(name = "credentials", singularKey = "credential", parser = { CredentialsParser(isAnonymous = false) })
    object CommentsType : MappingType(name = "comments", singularKey = "comment", parser = { CommentParser() })
    object DynamicSettingsType : MappingType(name = "settings", singularKey = "setting")
    object EditorialsType : MappingType(name = "editorials", singularKey = "editorial", parser = { EditorialParser() })
    object ErrorsType : MappingType(name = "errors", singularKey = "error")
    object ErrorType : MappingType(name = "error", singularKey = "error", pluralKey = "errors")
    object LovesType : MappingType(name = "loves", singularKey = "love")
    object NoContentType : MappingType(name = "204", singularKey = "204")
    object NotificationsType : MappingType(name = "notifications", singularKey = "notification", parser = { NotificationParser() })
    object PageHeadersType : MappingType(name = "page_headers", singularKey = "pageHeader", parser = { PageHeaderParser() })
    object PostsType : MappingType(name = "posts", singularKey = "post", parser = { PostParser() })
    object ProfilesType : MappingType(name = "profiles", singularKey = "profile", parser = { ProfileParser() })
    object RelationshipsType : MappingType(name = "relationships", singularKey = "relationship")
    object UsernamesType : MappingType(name = "usernames", singularKey = "username")
    object UsersType : MappingType(name = "users", singularKey = "user", parser = { UserParser() })
    object WatchesType : MappingType(name = "watches", singularKey = "watch", parser = { WatchParser() })

    companion object {
        fun create(value: String): MappingType? = when (value) {
            "activities"                -> MappingType.ActivitiesType
            "credentials"               -> MappingType.CredentialsType
            "announcements"             -> MappingType.AnnouncementsType
            "artist_invites"            -> MappingType.ArtistInvitesType
            "artist_invite_submissions" -> MappingType.ArtistInviteSubmissionsType
            "assets"                    -> MappingType.AssetsType
            "autocomplete_results"      -> MappingType.AutoCompleteResultType
            "availability"              -> MappingType.AvailabilityType
            "categories"                -> MappingType.CategoriesType
            "category_posts"            -> MappingType.CategoryPostsType
            "comments"                  -> MappingType.CommentsType
            "settings"                  -> MappingType.DynamicSettingsType
            "editorials"                -> MappingType.EditorialsType
            "notifications"             -> MappingType.NotificationsType
            "errors"                    -> MappingType.ErrorsType
            "error"                     -> MappingType.ErrorType
            "loves"                     -> MappingType.LovesType
            "204"                       -> MappingType.NoContentType
            "page_headers"              -> MappingType.PageHeadersType
            "posts"                     -> MappingType.PostsType
            "profiles"                  -> MappingType.ProfilesType
            "relationships"             -> MappingType.RelationshipsType
            "usernames"                 -> MappingType.UsernamesType
            "users"                     -> MappingType.UsersType
            "watches"                   -> MappingType.WatchesType
            else -> null
        }
    }
}
