package co.ello.android.ello


object PostTextPresenter {
    fun configure(cell: PostTextCell, item: StreamCellItem) {
        val data = item.type as? StreamCellType.PostText ?: return

        cell.config = PostTextCell.Config(
            content = data.content
            )
    }
}
