package co.ello.android.ello

object NotificationHeaderPresenter {
    fun configure(headerCell: NotificationHeaderCell, item: StreamCellItem) {
        val notification = item.model as? Notification ?: return

        headerCell.config(NotificationHeaderCell.Config(
                username = notification.author?.atName,
                avatarURL = notification.author?.avatarURL()
        ))
    }
}