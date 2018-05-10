package co.ello.android.ello


class PostFooterPresenter {
    companion object {
        fun configure(cell: PostFooterCell, item: StreamCellItem) {
            val post = item.model as? Post ?: return

            cell.config = PostFooterCell.Config(
                views = Count.Visible(0),
                comments = Count.Visible(0),
                loves = Count.Visible(0),
                reposts = Count.Visible(0),
                shareable = true
                )
        }
    }
}
