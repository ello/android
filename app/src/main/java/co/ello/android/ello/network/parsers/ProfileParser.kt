package co.ello.android.ello


class ProfileParser : IdParser(table = MappingType.ProfilesType) {
    override fun parse(json: JSON): Profile {
        val profile = Profile(
            id = json["id"].idValue,
            createdAt = json["createdAt"].date ?: Globals.now,
            shortBio = json["shortBio"].stringValue,
            email = json["email"].stringValue,
            confirmedAt = json["confirmedAt"].date ?: Globals.now,
            isPublic = json["isPublic"].booleanValue,
            isCommunity = json["isCommunity"].booleanValue,
            mutedCount = json["mutedCount"].intValue,
            blockedCount = json["blockedCount"].intValue,
            creatorTypeCategoryIds = json["creatorTypeCategoryIds"].listValue.flatMap { it.string?.let { listOf(it) } ?: emptyList() },
            hasSharingEnabled = json["hasSharingEnabled"].booleanValue,
            hasAdNotificationsEnabled = json["hasAdNotificationsEnabled"].booleanValue,
            hasAutoWatchEnabled = json["hasAutoWatchEnabled"].booleanValue,
            allowsAnalytics = json["allowsAnalytics"].booleanValue,
            notifyOfCommentsViaEmail = json["notifyOfCommentsViaEmail"].booleanValue,
            notifyOfLovesViaEmail = json["notifyOfLovesViaEmail"].booleanValue,
            notifyOfInvitationAcceptancesViaEmail = json["notifyOfInvitationAcceptancesViaEmail"].booleanValue,
            notifyOfMentionsViaEmail = json["notifyOfMentionsViaEmail"].booleanValue,
            notifyOfNewFollowersViaEmail = json["notifyOfNewFollowersViaEmail"].booleanValue,
            notifyOfRepostsViaEmail = json["notifyOfRepostsViaEmail"].booleanValue,
            notifyOfWhatYouMissedViaEmail = json["notifyOfWhatYouMissedViaEmail"].booleanValue,
            notifyOfApprovedSubmissionsFromFollowingViaEmail = json["notifyOfApprovedSubmissionsFromFollowingViaEmail"].booleanValue,
            notifyOfFeaturedCategoryPostViaEmail = json["notifyOfFeaturedCategoryPostViaEmail"].booleanValue,
            notifyOfFeaturedCategoryPostViaPush = json["notifyOfFeaturedCategoryPostViaPush"].booleanValue,
            subscribeToUsersEmailList = json["subscribeToUsersEmailList"].booleanValue,
            subscribeToDailyEllo = json["subscribeToDailyEllo"].booleanValue,
            subscribeToWeeklyEllo = json["subscribeToWeeklyEllo"].booleanValue,
            subscribeToOnboardingDrip = json["subscribeToOnboardingDrip"].booleanValue,
            notifyOfAnnouncementsViaPush = json["notifyOfAnnouncementsViaPush"].booleanValue,
            notifyOfApprovedSubmissionsViaPush = json["notifyOfApprovedSubmissionsViaPush"].booleanValue,
            notifyOfCommentsViaPush = json["notifyOfCommentsViaPush"].booleanValue,
            notifyOfLovesViaPush = json["notifyOfLovesViaPush"].booleanValue,
            notifyOfMentionsViaPush = json["notifyOfMentionsViaPush"].booleanValue,
            notifyOfRepostsViaPush = json["notifyOfRepostsViaPush"].booleanValue,
            notifyOfNewFollowersViaPush = json["notifyOfNewFollowersViaPush"].booleanValue,
            notifyOfInvitationAcceptancesViaPush = json["notifyOfInvitationAcceptancesViaPush"].booleanValue,
            notifyOfWatchesViaPush = json["notifyOfWatchesViaPush"].booleanValue,
            notifyOfWatchesViaEmail = json["notifyOfWatchesViaEmail"].booleanValue,
            notifyOfCommentsOnPostWatchViaPush = json["notifyOfCommentsOnPostWatchViaPush"].booleanValue,
            notifyOfCommentsOnPostWatchViaEmail = json["notifyOfCommentsOnPostWatchViaEmail"].booleanValue,
            notifyOfApprovedSubmissionsFromFollowingViaPush = json["notifyOfApprovedSubmissionsFromFollowingViaPush"].booleanValue,
            hasAnnouncementsEnabled = json["hasAnnouncementsEnabled"].booleanValue,
            discoverable = json["discoverable"].booleanValue,
            gaUniqueId = json["gaUniqueId"].string
        )

        profile.mergeLinks(json["links"])

        return profile
    }
}
