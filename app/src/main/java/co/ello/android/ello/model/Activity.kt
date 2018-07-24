package co.ello.android.ello

import java.util.Date



data class Activity(
    val id: String,
    val createdAt: Date,
    val kind: Activity.Kind,
    val subjectType: Activity.SubjectType
    ) : Model() {

    val subject: Model? get() = getLinkObject("subject")
    override val identifier = Parser.Identifier(id = id, table = MappingType.ActivitiesType)
    override fun update(property: Property, value: Any) {}

    enum class Kind(val value: String) {
        NewFollowerPost("new_follower_post"), // someone started following you
        NewFollowedUserPost("new_followed_user_post"), // you started following someone
        InvitationAcceptedPost("invitation_accepted_post"), // someone accepted your invitation

        PostMentionNotification("post_mention_notification"), // you were mentioned in a post
        CommentMentionNotification("comment_mention_notification"), // you were mentioned in a comment
        CommentNotification("comment_notification"), // someone commented on your post
        CommentOnOriginalPostNotification("comment_on_original_post_notification"), // someone commented on your repost
        CommentOnRepostNotification("comment_on_repost_notification"), // someone commented on other's repost of your post

        WelcomeNotification("welcome_notification"), // welcome to Ello
        RepostNotification("repost_notification"), // someone reposted your post

        WatchNotification("watch_notification"), // someone watched your post on ello
        WatchCommentNotification("watch_comment_notification"), // someone commented on a post you're watching
        WatchOnRepostNotification("watch_on_repost_notification"), // someone watched your repost
        WatchOnOriginalPostNotification("watch_on_original_post_notification"), // someone watched other's repost of your post

        LoveNotification("love_notification"), // someone loved your post
        LoveOnRepostNotification("love_on_repost_notification"), // someone loved your repost
        LoveOnOriginalPostNotification("love_on_original_post_notification"), // someone loved other's repost of your post

        ApprovedArtistInviteSubmission("approved_artist_invite_submission"), // your submission has been accepted
        ApprovedArtistInviteSubmissionNotificationForFollowers("approved_artist_invite_submission_notification_for_followers"); // a person you follow had their submission accepted

        companion object {
            fun create(value: String): Kind? = when(value) {
                "new_follower_post" -> NewFollowerPost
                "new_followed_user_post" -> NewFollowedUserPost
                "invitation_accepted_post" -> InvitationAcceptedPost
                "post_mention_notification" -> PostMentionNotification
                "comment_mention_notification" -> CommentMentionNotification
                "comment_notification" -> CommentNotification
                "comment_on_original_post_notification" -> CommentOnOriginalPostNotification
                "comment_on_repost_notification" -> CommentOnRepostNotification
                "welcome_notification" -> WelcomeNotification
                "repost_notification" -> RepostNotification
                "watch_notification" -> WatchNotification
                "watch_comment_notification" -> WatchCommentNotification
                "watch_on_repost_notification" -> WatchOnRepostNotification
                "watch_on_original_post_notification" -> WatchOnOriginalPostNotification
                "love_notification" -> LoveNotification
                "love_on_repost_notification" -> LoveOnRepostNotification
                "love_on_original_post_notification" -> LoveOnOriginalPostNotification
                "approved_artist_invite_submission" -> ApprovedArtistInviteSubmission
                "approved_artist_invite_submission_notification_for_followers" -> ApprovedArtistInviteSubmissionNotificationForFollowers
                else -> null
            }
        }
    }

    enum class SubjectType(val value: String) {
        User("User"),
        Post("Post"),
        Comment("Comment");

        companion object {
            fun create(value: String): SubjectType? = when(value) {
                "User" -> User
                "Post" -> Post
                "Comment" -> Comment
                else -> null
            }
        }
    }
}