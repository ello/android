package co.ello.android.ello


object PostHeaderPresenter {
    fun configure(cell: PostHeaderCell, item: StreamCellItem) {
        val post = item.model as? Post ?: return

        cell.config = PostHeaderCell.Config(
            username = post.author?.atName,
            avatarURL = post.author?.avatarURL(),
            postedAt = post.createdAt
            )
    }
}
