package co.ello.android.ello


class UserParser : IdParser(table = MappingType.UsersType) {
    init {
        linkObject(MappingType.ProfilesType)
        linkArray(MappingType.CategoriesType)
    }

    override fun parse(json: JSON): User {
        val relationshipPriority = RelationshipPriority.create(json["currentUserState"]["relationshipPriority"].string)

        val user = User(
            id = json["id"].idValue,
            username = json["username"].stringValue,
            name = json["name"].stringValue,
            relationshipPriority = relationshipPriority,
            postsAdultContent = json["settings"]["postsAdultContent"].booleanValue,
            viewsAdultContent = json["settings"]["viewsAdultContent"].booleanValue,
            hasCommentingEnabled = json["settings"]["hasCommentingEnabled"].booleanValue,
            hasSharingEnabled = json["settings"]["hasSharingEnabled"].booleanValue,
            hasRepostingEnabled = json["settings"]["hasRepostingEnabled"].booleanValue,
            hasLovesEnabled = json["settings"]["hasLovesEnabled"].booleanValue,
            isCollaborateable = json["settings"]["isCollaborateable"].booleanValue,
            isHireable = json["settings"]["isHireable"].booleanValue
        )

        user.avatar = AssetParser().parse(json, lookIn = "avatar")
        user.coverImage = AssetParser().parse(json, lookIn = "coverImage")

        // user.identifiableBy = json["identifiable_by"].string
        user.formattedShortBio = json["formattedShortBio"].string
        // user.onboardingVersion = json["web_onboarding_version"].id.flatMap { Int($0) }
        user.location = json["location"].string

        json["externalLinksList"].list?.let { jsonLinks ->
            user.externalLinksList = jsonLinks.flatMap { ExternalLink.fromJSON(it)?.let { listOf(it) } ?: emptyList() }
        }

        json["badges"].list?.let { badgeNames ->
            user.badges = badgeNames.flatMap { it.string?.let { Badge.lookup(it) }?.let { listOf(it) } ?: emptyList() }
        }

        user.totalViewsCount = json["userStats"]["totalViewsCount"].int
        user.postsCount = json["userStats"]["postsCount"].int
        user.lovesCount = json["userStats"]["lovesCount"].int
        user.followersCount = json["userStats"]["followersCount"].int
        user.followingCount = json["userStats"]["followingCount"].int

        user.mergeLinks(json["links"])

        return user
    }
}
