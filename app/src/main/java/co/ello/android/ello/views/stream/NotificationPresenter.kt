package co.ello.android.ello

object NotificationPresenter {
    fun configure(cell: NotificationImageTextCell, item: StreamCellItem) {
        val notification = item.model as? Notification ?: return
        cell.config(NotificationImageTextCell.Config(
                notificationTitle = NotificationAttributedTitle.from(notification),
                avatarURL = notification.author?.avatarURL(),
                hasImage = notification.hasImage,
                contentImageURL = notification.imageRegion?.url,
                contentText = notification.textRegion?.content,
                postActionable = notification.canReplyToComment,
                postedAt = notification.createdAt
        ))
    }
}