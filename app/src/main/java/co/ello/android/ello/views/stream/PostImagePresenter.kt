package co.ello.android.ello

import java.net.URL


object PostImagePresenter {
    fun configure(cell: PostImageCell, item: StreamCellItem) {
        var imageRegion =
            (item.type as? StreamCellType.PostImage)?.image ?:
            (item.type as? StreamCellType.CommentImage)?.image ?:
            return
        val asset = imageRegion.asset ?: return

        cell.config(PostImageCell.Config(
            imageURL = imageRegion.fullScreenURL,
            isGif = asset.isGif
            ))
    }
}
