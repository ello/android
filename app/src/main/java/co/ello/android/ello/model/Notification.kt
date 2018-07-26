package co.ello.android.ello

import java.text.DateFormat
import java.time.LocalDate


data class Notification(
        val id: String,
        val createdAt: String,
        val kind: Kind?,
        val subjectType: SubjectType?
    ) : Model() {

    var author: User? = null
    var postId: String? = null
    val subject: Model? get() = getLinkObject("subject")

    // notification specific
    var textRegion: TextRegion? = null
    var imageRegion: ImageRegion? = null

    val hasImage: Boolean get() = this.imageRegion != null
    val canReplyToComment: Boolean get() = when(kind) {
        Kind.PostMentionNotification, Kind.CommentNotification,
        Kind.CommentMentionNotification, Kind.CommentOnOriginalPostNotification,
        Kind.CommentOnRepostNotification -> true
        else -> false
    }

    init {
        val post = subject as? Post
        val comment = subject as? Comment
        val user = subject as? User
        val actionable = subject as? PostActionable
        val actionablePost = actionable?.post

        if (post != null) {
            this.author = post.author
            this.postId = post.id
        }
        else if (comment != null) {
            this.author = comment.author
            this.postId = comment.postId
        }
        else if (user != null) {
            this.author = user
        }
        else if (actionable != null) {
            this.postId = actionable.postId
            val actionableUser = actionable.user
            if (actionableUser  != null) {
                this.author = actionableUser
            }
        }

        if (post != null) {
            assignRegionsFromContent(post.summary)
        }
        else if (comment != null) {
            var parentSummary = comment.parentPost?.summary
            var summary = if (comment.summary.isNotEmpty()) comment.summary else comment.content

            assignRegionsFromContent(content = summary, parentSummary = parentSummary)
        }
        else if (actionablePost != null) {
            assignRegionsFromContent(actionablePost.summary)
        }


        textRegion = TextRegion(id)
    }

    private fun assignRegionsFromContent(content: List<Regionable>, parentSummary: List<Regionable>? = null) {
        // assign textRegion and imageRegion from the post content - finds
        // the first of both kinds of regions
        val textContent: MutableList<String> = mutableListOf()
        var contentImage: ImageRegion? = null
        var parentImage: ImageRegion? = null

        if (parentSummary != null) {
            for (region in parentSummary) {
                val newTextRegion = region as? TextRegion
                val newImageRegion = region as? ImageRegion

                if (newTextRegion != null) {
                    textContent.add(newTextRegion.content)
                }
                else if (newImageRegion != null && parentImage == null) {
                    parentImage = newImageRegion
                }
            }
        }

        for (region in content) {
            val newTextRegion = region as? TextRegion
            val newImageRegion = region as? ImageRegion

            if (newTextRegion != null) {
                textContent.add(newTextRegion.content)
            }
            else if (newImageRegion != null && contentImage == null) {
                contentImage = newImageRegion
            }
        }

        imageRegion = contentImage ?: parentImage
        textRegion = TextRegion(textContent.joinToString("<br/>"))
    }

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
