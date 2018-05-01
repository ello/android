package co.ello.android.ello

import java.util.Date


data class Notification(
    val activity: Activity
    ) : Model() {

    var author: User? = null
    var postId: String? = null

    val createdAt: Date get() = activity.createdAt
    var subject: Model?

    // notification specific
    var textRegion: TextRegion? = null
    var imageRegion: ImageRegion? = null

    val hasImage: Boolean get() = this.imageRegion != null
    val canReplyToComment: Boolean get() = when(activity.kind) {
        Activity.Kind.PostMentionNotification, Activity.Kind.CommentNotification,
        Activity.Kind.CommentMentionNotification, Activity.Kind.CommentOnOriginalPostNotification,
        Activity.Kind.CommentOnRepostNotification -> true
        else -> false
    }

    init {
        val post = activity.subject as? Post
        val comment = activity.subject as? Comment
        val user = activity.subject as? User
        val actionable = activity.subject as? PostActionable
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

        subject = activity.subject
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

}
