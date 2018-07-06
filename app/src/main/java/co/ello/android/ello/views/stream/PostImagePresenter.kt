package co.ello.android.ello

import java.net.URL


object PostImagePresenter {
    fun configure(cell: PostImageCell, item: StreamCellItem) {
        // val post = item.model as? Post ?: return
        var data: URL? = null
        (item.type as? StreamCellType.PostImage)?.let { data = it.imageURL }
        (item.type as? StreamCellType.CommentImage)?.let { data = it.imageURL }
        val url = data ?: return

        cell.config(PostImageCell.Config(
                        imageURL = url
            ))
    }
}
