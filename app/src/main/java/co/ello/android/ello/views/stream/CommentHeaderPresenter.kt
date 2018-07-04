package co.ello.android.ello


object CommentHeaderPresenter {
    fun configure(cell: CommentHeaderCell, item: StreamCellItem) {
        val comment = item.model as? Comment ?: return

        cell.config(CommentHeaderCell.Config(
            username = comment.author?.atName,
            avatarURL = comment.author?.avatarURL(),
            postedAt = comment.createdAt
            ))
    }
}
