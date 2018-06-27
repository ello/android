package co.ello.android.ello


object PostFooterPresenter {
    fun configure(cell: PostFooterCell, item: StreamCellItem) {
        val post = item.model as? Post ?: return

        cell.config(PostFooterCell.Config(
            views = Count.Visible(post.viewsCount ?: 0),
            loves = Count.Visible(post.lovesCount ?: 0),
            comments = Count.Visible(post.commentsCount ?: 0),
            reposts = Count.Visible(post.repostsCount ?: 0),
            shareable = true
            ))
    }
}
