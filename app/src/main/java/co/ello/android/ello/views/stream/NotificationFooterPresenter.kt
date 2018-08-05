package co.ello.android.ello

object NotificationFooterPresenter {
    fun configure(footerCell: NotificationFooterCell, item: StreamCellItem) {
        val notification = item.model as? Notification ?: return
        footerCell.config(NotificationFooterCell.Config(
                postedAt = notification.createdAt
        ))
    }
}