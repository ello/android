package co.ello.android.ello

object NotificationPresenter {
    fun configure(cell: NotificationCell, item: StreamCellItem) {
        val notification = item.model as? Notification ?: return

        cell.config(NotificationCell.Config(
                username = notification.author?.atName,
                avatarURL = notification.author?.avatarURL(),
                postedAt = notification.createdAt
        ))
    }
}