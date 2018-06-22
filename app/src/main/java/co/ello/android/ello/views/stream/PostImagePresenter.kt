package co.ello.android.ello


object PostImagePresenter {
    fun configure(cell: PostImageCell, item: StreamCellItem) {
        // val post = item.model as? Post ?: return
        val data = item.type as? StreamCellType.PostImage ?: return

        cell.config = PostImageCell.Config(
            imageURL = data.imageURL
            )
    }
}
