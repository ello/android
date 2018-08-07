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
                    return Html.fromHtml("Placeholder")
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
                    if ((subject as? Love) != null){
                        val post = (subject as? Love)?.post
                        return Html.fromHtml(author?.atName + " loved your <u>repost</u>.")
                    }
                    else {
                        return Html.fromHtml(author?.atName + " loved your repost.")
                    }
                }
                Notification.Kind.LoveOnOriginalPostNotification -> {
                    return Html.fromHtml("Placeholder")
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
                    return Html.fromHtml("Placeholder")
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
                Notification.Kind.WelcomeNotification -> {
                    Html.fromHtml("Welcome to Ello!")
                }

                else -> return Html.fromHtml("")
            }
            return null
        }
    }
}