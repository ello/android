package co.ello.android.ello

import java.util.Date


data class Profile(
    val id: String,
    val createdAt: Date,
    val shortBio: String,
    val email: String,
    val confirmedAt: Date,
    val isPublic: Boolean,
    val isCommunity: Boolean,
    val mutedCount: Int,
    val blockedCount: Int,
    val creatorTypeCategoryIds: List<String>,
    val hasSharingEnabled: Boolean,
    val hasAdNotificationsEnabled: Boolean,
    val hasAutoWatchEnabled: Boolean,
    val allowsAnalytics: Boolean,
    val notifyOfCommentsViaEmail: Boolean,
    val notifyOfLovesViaEmail: Boolean,
    val notifyOfInvitationAcceptancesViaEmail: Boolean,
    val notifyOfMentionsViaEmail: Boolean,
    val notifyOfNewFollowersViaEmail: Boolean,
    val notifyOfRepostsViaEmail: Boolean,
    val notifyOfWhatYouMissedViaEmail: Boolean,
    val notifyOfApprovedSubmissionsFromFollowingViaEmail: Boolean,
    val notifyOfFeaturedCategoryPostViaEmail: Boolean,
    val notifyOfFeaturedCategoryPostViaPush: Boolean,
    val subscribeToUsersEmailList: Boolean,
    val subscribeToDailyEllo: Boolean,
    val subscribeToWeeklyEllo: Boolean,
    val subscribeToOnboardingDrip: Boolean,
    val notifyOfAnnouncementsViaPush: Boolean,
    val notifyOfApprovedSubmissionsViaPush: Boolean,
    val notifyOfCommentsViaPush: Boolean,
    val notifyOfLovesViaPush: Boolean,
    val notifyOfMentionsViaPush: Boolean,
    val notifyOfRepostsViaPush: Boolean,
    val notifyOfNewFollowersViaPush: Boolean,
    val notifyOfInvitationAcceptancesViaPush: Boolean,
    val notifyOfWatchesViaPush: Boolean,
    val notifyOfWatchesViaEmail: Boolean,
    val notifyOfCommentsOnPostWatchViaPush: Boolean,
    val notifyOfCommentsOnPostWatchViaEmail: Boolean,
    val notifyOfApprovedSubmissionsFromFollowingViaPush: Boolean,
    val hasAnnouncementsEnabled: Boolean,
    val discoverable: Boolean,
    val gaUniqueId: String?
    ) : Model() {

    sealed class CreatorType {
        object None : CreatorType()
        object Fan : CreatorType()
        data class Artist(val selections: List<Category>) : CreatorType()

        val isValid: Boolean get() = when(this) {
            is None -> false
            is Fan -> true
            is Artist -> this.selections.isNotEmpty()
        }
    }

    enum class ImageProperty {
        Avatar, CoverImage;

        val toProperty: Property get() = when(this) {
            Avatar -> Property.AvatarURL
            CoverImage -> Property.CoverImageURL
        }
    }

    enum class Property(val value: String) {
        Name("name"),
        Username("username"),
        Email("email"),
        Bio("unsanitized_short_bio"),
        Links("external_links"),
        Location("location"),
        AvatarURL("remote_avatar_url"),
        CoverImageURL("remote_cover_image_url"),
        WebOnboardingVersion("web_onboarding_version"),
        CreatorTypeCategoryIds("creator_type_category_ids"),
        CurrentPassword("current_password"),
        Password("password"),
        PasswordConfirmation("password_confirmation"),
        AllowsAnalytics("allows_analytics"),
        Discoverable("discoverable"),
        HasAdNotificationsEnabled("has_ad_notifications_enabled"),
        HasAnnouncementsEnabled("has_announcements_enabled"),
        HasAutoWatchEnabled("has_auto_watch_enabled"),
        HasRepostingEnabled("has_reposting_enabled"),
        HasSharingEnabled("has_sharing_enabled"),
        IsCollaborateable("is_collaborateable"),
        IsHireable("is_hireable"),
        NotifyOfAnnouncementsViaPush("notify_of_announcements_via_push"),
        NotifyOfApprovedSubmissionsFromFollowingViaEmail("notify_of_approved_submissions_from_following_via_email"),
        NotifyOfFeaturedCategoryPostViaEmail("notify_of_featured_category_post_via_email"),
        NotifyOfFeaturedCategoryPostViaPush("notify_of_featured_category_post_via_push"),
        NotifyOfApprovedSubmissionsFromFollowingViaPush("notify_of_approved_submissions_from_following_via_push"),
        NotifyOfApprovedSubmissionsViaPush("notify_of_approved_submissions_via_push"),
        NotifyOfCommentsOnPostWatchViaEmail("notify_of_comments_on_post_watch_via_email"),
        NotifyOfCommentsOnPostWatchViaPush("notify_of_comments_on_post_watch_via_push"),
        NotifyOfCommentsViaEmail("notify_of_comments_via_email"),
        NotifyOfCommentsViaPush("notify_of_comments_via_push"),
        NotifyOfInvitationAcceptancesViaEmail("notify_of_invitation_acceptances_via_email"),
        NotifyOfInvitationAcceptancesViaPush("notify_of_invitation_acceptances_via_push"),
        NotifyOfLovesViaEmail("notify_of_loves_via_email"),
        NotifyOfLovesViaPush("notify_of_loves_via_push"),
        NotifyOfMentionsViaEmail("notify_of_mentions_via_email"),
        NotifyOfMentionsViaPush("notify_of_mentions_via_push"),
        NotifyOfNewFollowersViaEmail("notify_of_new_followers_via_email"),
        NotifyOfNewFollowersViaPush("notify_of_new_followers_via_push"),
        NotifyOfRepostsViaEmail("notify_of_reposts_via_email"),
        NotifyOfRepostsViaPush("notify_of_reposts_via_push"),
        NotifyOfWatchesViaEmail("notify_of_watches_via_email"),
        NotifyOfWatchesViaPush("notify_of_watches_via_push"),
        NotifyOfWhatYouMissedViaEmail("notify_of_what_you_missed_via_email"),
        SubscribeToDailyEllo("subscribe_to_daily_ello"),
        SubscribeToOnboardingDrip("subscribe_to_onboarding_drip"),
        SubscribeToUsersEmailList("subscribe_to_users_email_list"),
        SubscribeToWeeklyEllo("subscribe_to_weekly_ello");

        companion object {
            fun create(value: String): Property? = when(value) {
                "name" -> Property.Name
                "username" -> Property.Username
                "email" -> Property.Email
                "unsanitized_short_bio" -> Property.Bio
                "external_links" -> Property.Links
                "location" -> Property.Location
                "remote_avatar_url" -> Property.AvatarURL
                "remote_cover_image_url" -> Property.CoverImageURL
                "web_onboarding_version" -> Property.WebOnboardingVersion
                "creator_type_category_ids" -> Property.CreatorTypeCategoryIds
                "current_password" -> Property.CurrentPassword
                "password" -> Property.Password
                "password_confirmation" -> Property.PasswordConfirmation
                "allows_analytics" -> Property.AllowsAnalytics
                "discoverable" -> Property.Discoverable
                "has_ad_notifications_enabled" -> Property.HasAdNotificationsEnabled
                "has_announcements_enabled" -> Property.HasAnnouncementsEnabled
                "has_auto_watch_enabled" -> Property.HasAutoWatchEnabled
                "has_reposting_enabled" -> Property.HasRepostingEnabled
                "has_sharing_enabled" -> Property.HasSharingEnabled
                "is_collaborateable" -> Property.IsCollaborateable
                "is_hireable" -> Property.IsHireable
                "notify_of_announcements_via_push" -> Property.NotifyOfAnnouncementsViaPush
                "notify_of_approved_submissions_from_following_via_email" -> Property.NotifyOfApprovedSubmissionsFromFollowingViaEmail
                "notify_of_featured_category_post_via_email" -> Property.NotifyOfFeaturedCategoryPostViaEmail
                "notify_of_featured_category_post_via_push" -> Property.NotifyOfFeaturedCategoryPostViaPush
                "notify_of_approved_submissions_from_following_via_push" -> Property.NotifyOfApprovedSubmissionsFromFollowingViaPush
                "notify_of_approved_submissions_via_push" -> Property.NotifyOfApprovedSubmissionsViaPush
                "notify_of_comments_on_post_watch_via_email" -> Property.NotifyOfCommentsOnPostWatchViaEmail
                "notify_of_comments_on_post_watch_via_push" -> Property.NotifyOfCommentsOnPostWatchViaPush
                "notify_of_comments_via_email" -> Property.NotifyOfCommentsViaEmail
                "notify_of_comments_via_push" -> Property.NotifyOfCommentsViaPush
                "notify_of_invitation_acceptances_via_email" -> Property.NotifyOfInvitationAcceptancesViaEmail
                "notify_of_invitation_acceptances_via_push" -> Property.NotifyOfInvitationAcceptancesViaPush
                "notify_of_loves_via_email" -> Property.NotifyOfLovesViaEmail
                "notify_of_loves_via_push" -> Property.NotifyOfLovesViaPush
                "notify_of_mentions_via_email" -> Property.NotifyOfMentionsViaEmail
                "notify_of_mentions_via_push" -> Property.NotifyOfMentionsViaPush
                "notify_of_new_followers_via_email" -> Property.NotifyOfNewFollowersViaEmail
                "notify_of_new_followers_via_push" -> Property.NotifyOfNewFollowersViaPush
                "notify_of_reposts_via_email" -> Property.NotifyOfRepostsViaEmail
                "notify_of_reposts_via_push" -> Property.NotifyOfRepostsViaPush
                "notify_of_watches_via_email" -> Property.NotifyOfWatchesViaEmail
                "notify_of_watches_via_push" -> Property.NotifyOfWatchesViaPush
                "notify_of_what_you_missed_via_email" -> Property.NotifyOfWhatYouMissedViaEmail
                "subscribe_to_daily_ello" -> Property.SubscribeToDailyEllo
                "subscribe_to_onboarding_drip" -> Property.SubscribeToOnboardingDrip
                "subscribe_to_users_email_list" -> Property.SubscribeToUsersEmailList
                "subscribe_to_weekly_ello" -> Property.SubscribeToWeeklyEllo
                else -> null
            }
        }
    }
}
