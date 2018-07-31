package co.ello.android.ello


object PostTextPresenter {
    fun configure(cell: PostTextCell, item: StreamCellItem) {
        var dataContent: String? = null
        (item.type as? StreamCellType.PostText)?.let { dataContent = it.content }
        (item.type as? StreamCellType.CommentText)?.let { dataContent = it.content }
        val content = dataContent ?: return

        cell.config(PostTextCell.Config(
            content = content
            ))
    }
}
