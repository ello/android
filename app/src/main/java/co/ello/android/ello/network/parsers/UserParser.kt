package co.ello.android.ello


class UserParser : IdParser(table = MappingType.UsersType) {
    init {
        linkObject(MappingType.ProfilesType)
        linkArray(MappingType.CategoriesType)
    }

    override fun parse(json: JSON): User {
        val relationshipPriority = RelationshipPriority.create(json["currentUserState"]["relationshipPriority"].stringValue) ?: RelationshipPriority.None

        val user = User(
            id = json["id"].stringValue,
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

        user.avatar = AssetParser().parse(json["avatar"])
        user.coverImage = AssetParser().parse(json["coverImage"])

        // user.identifiableBy = json["identifiable_by"].string
        user.formattedShortBio = json["formattedShortBio"].string
        // user.onboardingVersion = json["web_onboarding_version"].id.flatMap { Int($0) }
        user.totalViewsCount = json["stats"]["totalViewsCount"].int
        user.location = json["location"].string

        json["externalLinksList"].list?.let { jsonLinks ->
            user.externalLinksList = jsonLinks.flatMap { ExternalLink.fromJSON(it)?.let { listOf(it) } ?: emptyList() }
        }

        json["badges"].list?.let { badgeNames ->
            user.badges = badgeNames.flatMap { it.string?.let { Badge.lookup(it) }?.let { listOf(it) } ?: emptyList() }
        }

        if (relationshipPriority == RelationshipPriority.Me && json["profile"].exists()) {
            user.profile = ProfileParser().parse(json["profile"])
        }

        user.postsCount = json["userStats"]["postsCount"].int
        user.lovesCount = json["userStats"]["lovesCount"].int
        user.followersCount = json["userStats"]["followersCount"].string
        user.followingCount = json["userStats"]["followingCount"].int

        user.mergeLinks(json["links"])

        return user
    }
}
