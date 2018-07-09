package co.ello.android.ello

import java.net.URL


object PostEmbedPresenter {
    fun configure(cell: PostEmbedCell, item: StreamCellItem) {
        // val post = item.model as? Post ?: return
        var data: EmbedRegion? = null
        (item.type as? StreamCellType.PostEmbed)?.let { data = it }
        (item.type as? StreamCellType.CommentEmbed)?.let { data = it }
        val region = data ?: return

        cell.config(PostEmbedCell.Config(
            imageURL = region.thumbnailLargeUrl,
            externalURL = region.url
            ))
    }
}
