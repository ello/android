package co.ello.android.ello

object NotificationPresenter {
    fun configure(cell: NotificationImageTextCell, item: StreamCellItem) {
        val notification = item.model as? Notification ?: return
        cell.config(NotificationImageTextCell.Config(notification.imageRegion?.url, notification.textRegion?.content))
    }
}