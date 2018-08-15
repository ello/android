package co.ello.android.ello

import android.text.Html
import android.text.Spanned

class NotificationAttributedTitle {
    companion object {
        fun from(notification: Notification): Spanned? {
            val kind = notification.kind
            val author = notification.author
            val subject = notification.subject

            when (kind) {
                Notification.Kind.RepostNotification -> {
                    if ((subject as? Post) != null){
                        return Html.fromHtml(author?.atName + " reposted your <u>post</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " reposted your post.")
                    }
                }
                Notification.Kind.NewFollowedUserPost -> {
                    return Html.fromHtml("You started following " +"<u>"+ author?.atName +"</u>.")
                }
                Notification.Kind.NewFollowerPost -> {
                    return Html.fromHtml("<u>"+ author?.atName +"</u> started following you.")
                }
                Notification.Kind.PostMentionNotification -> {
                    if ((subject as? Post) != null){
                        return Html.fromHtml(author?.atName + " mentioned you in a <u>post</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " mentioned you in a post.")
                    }
                }
                Notification.Kind.CommentNotification -> {
                    if ((subject as? Comment) != null){
                        return Html.fromHtml(author?.atName + " commented on your <u>post</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " commented on a post.")
                    }
                }
                Notification.Kind.CommentMentionNotification -> {
                    if ((subject as? Comment) != null){
                        return Html.fromHtml(author?.atName + " mentioned you in a <u>comment</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " mentioned you in a comment.")
                    }
                }
                Notification.Kind.CommentOnOriginalPostNotification -> {
                    if ((subject as? Comment != null)) {
                        val comment = subject
                        val repost = comment.loadedFromPost
                        val repostAuthor = repost?.author
                        val source = repost?.repostSource
                        return Html.fromHtml(author?.atName + " commented on " +repostAuthor?.atName + "'s <u>repost</u> of your <u>post</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " commented on your post.")
                    }
                }
                Notification.Kind.CommentOnRepostNotification -> {
                    if ((subject as? Comment) != null){
                        return Html.fromHtml(author?.atName + " commented on your <u>repost</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " commented on your repost.")
                    }
                }
                Notification.Kind.InvitationAcceptedPost -> {
                    return Html.fromHtml(author?.atName + "accepted your invitation.")
                }
                Notification.Kind.LoveNotification -> {
                    if ((subject as? Love) != null){
                        val post = (subject as? Love)?.post
                        return Html.fromHtml(author?.atName + " loved your <u>post</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " loved your post.")
                    }
                }
                Notification.Kind.LoveOnRepostNotification -> {
                    if ((subject as? Love != null)) {
                        val love = subject
                        val post = love.post
                        return Html.fromHtml(author?.atName + " loved your <u>repost</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " loved your repost.")
                    }
                }
                Notification.Kind.LoveOnOriginalPostNotification -> {
                    if ((subject as? Love != null)) {
                        val love = subject
                        val repost = love.post
                        val repostAuthor = repost?.author
                        val source = repost?.repostSource
                        return Html.fromHtml(author?.atName + " loved " +repostAuthor?.atName + "'s <u>repost</u> of your <u>post</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " loved a repost of your post.")
                    }
                }
                Notification.Kind.WatchNotification -> {
                    if ((subject as? Watch) != null){
                        val post = (subject as? Watch)?.post
                        return Html.fromHtml(author?.atName + " is watching your <u>post</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " is watching your post.")
                    }
                }
                Notification.Kind.WatchCommentNotification -> {
                    if ((subject as? Comment) != null){
                        val post = (subject as? Comment)?.parentPost
                        return Html.fromHtml(author?.atName + " commented on a <u>post</u> you're watching.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " commented on a post you're watching.")
                    }
                }
                Notification.Kind.WatchOnRepostNotification -> {
                    if ((subject as? Watch) != null){
                        val post = (subject as? Watch)?.post
                        return Html.fromHtml(author?.atName + " is watching your <u>repost</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " is watching your repost.")
                    }
                }
                Notification.Kind.WatchOnOriginalPostNotification -> {
                    if ((subject as? Watch != null)) {
                        val watch = subject
                        val repost = watch.post
                        val repostAuthor = repost?.author
                        val source = repost?.repostSource
                        return Html.fromHtml(author?.atName + " is watching " +repostAuthor?.atName + "'s <u>repost</u> of your <u>post</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " is watching a repost of your post.")
                    }
                }
                Notification.Kind.ApprovedArtistInviteSubmission -> {
                    if ((subject as? ArtistInviteSubmission) != null){
                        val artistInvite = (subject as? ArtistInviteSubmission)?.artistInvite
                        return Html.fromHtml("Your submission to <u>" +artistInvite +"</u> has been accepted!")
                    }
                    else {
                        return Html.fromHtml("Your submission has been accepted!")
                    }
                }
                Notification.Kind.ApprovedArtistInviteSubmissionNotificationForFollowers -> {
                    if ((subject as? ArtistInviteSubmission) != null){
                        val artistInvite = (subject as? ArtistInviteSubmission)?.artistInvite
                        val author = (subject as? ArtistInviteSubmission)?.post?.author
                        return Html.fromHtml(author?.atName + "'s  submission to <u>" +artistInvite +"</u> has been accepted!")
                    }
                    else {
                        return Html.fromHtml("A follower's submission has been accepted!")
                    }
                }
                Notification.Kind.CategoryPostFeatured -> {
                    if ((subject as? CategoryPost) != null){
                        val submission = subject
                        val featuredBy = submission.featuredBy
                        val categoryText = submission.category?.name
                        val post = submission.post
                        return Html.fromHtml(featuredBy?.atName + "featured your <u>post</u> in <u>" +categoryText +"</u>.")
                    }
                    else {
                        return Html.fromHtml("Someone featured your post.")
                    }
                }
                Notification.Kind.CategoryRepostFeatured -> {
                    if ((subject as? CategoryPost) != null){
                        val submission = subject
                        val featuredBy = submission.featuredBy
                        val categoryText = submission.category?.name
                        val post = submission.post
                        return Html.fromHtml(featuredBy?.atName + "featured your <u>repost</u> in <u>" +categoryText +"</u>.")
                    }
                    else {
                        return Html.fromHtml("Someone featured your repost.")
                    }
                }
                Notification.Kind.CategoryPostViaRepostFeatured -> {
                    if ((subject as? CategoryPost) != null){
                        val submission = subject
                        val featuredBy = submission.featuredBy
                        val categoryText = submission.category?.name
                        val repost = submission.post
                        val source = repost?.repostSource
                        return Html.fromHtml(featuredBy?.atName + "featured a <u>repost</u> of your <u>post</u> in <u>" +categoryText +"</u>.")
                    }
                    else {
                        return Html.fromHtml("Someone featured a repost of your post.")
                    }
                }
                Notification.Kind.UserAddedAsFeatured -> {
                    if ((subject as? CategoryUser) != null){
                        val submission = subject
                        val featuredBy = submission.featuredBy
                        val categoryText = submission.category?.name
                        return Html.fromHtml(featuredBy?.atName + " has featured you in <u>" +categoryText +"</u>.")
                    }
                    else {
                        return Html.fromHtml("Someone has featured you in a category.")
                    }
                }
                Notification.Kind.UserAddedAsCurator -> {
                    if ((subject as? CategoryUser) != null){
                        val submission = subject
                        val curatorBy = submission.curatorBy
                        val categoryText = submission.category?.name
                        return Html.fromHtml(curatorBy?.atName + " has invited you to help curate <u>" +categoryText +"</u>.")
                    }
                    else {
                        return Html.fromHtml("Someone has invited you to help curate a category.")
                    }
                }
                Notification.Kind.UserAddedAsModerator -> {
                    if ((subject as? CategoryUser) != null){
                        val submission = subject
                        val moderatorBy = submission.moderatorBy
                        val categoryText = submission.category?.name
                        return Html.fromHtml(moderatorBy?.atName + " has invited you to help moderate <u>" +categoryText +"</u>.")
                    }
                    else {
                        return Html.fromHtml("Someone has invited you to help moderate a category.")
                    }
                }
                Notification.Kind.WelcomeNotification -> {
                    Html.fromHtml("Welcome to Ello!")
                }

                else -> return Html.fromHtml("")
            }
            return null
        }
    }
}