package co.ello.android.ello

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import java.net.URL


sealed class StreamCellType {
    object PostHeader : StreamCellType()
    object PostFooter : StreamCellType()
    data class PostText(val content: String) : StreamCellType()
    data class PostImage(val imageURL: URL) : StreamCellType()

    val viewType: Int get() = StreamCellType::class.nestedClasses.indexOf(this::class)

    fun bindViewHolder(viewHolder: StreamCell, item: StreamCellItem) {
        viewHolder.streamCellItem = item

        return when(this) {
            is PostHeader -> PostHeaderPresenter.configure(viewHolder as PostHeaderCell, item)
            is PostFooter -> PostFooterPresenter.configure(viewHolder as PostFooterCell, item)
            is PostText -> PostTextPresenter.configure(viewHolder as PostTextCell, item)
            is PostImage -> PostImagePresenter.configure(viewHolder as PostImageCell, item)
        }
    }

    companion object {
        fun createViewHolder(parent: ViewGroup, viewType: Int): StreamCell {
            val type = StreamCellType::class.nestedClasses.elementAt(viewType)
            return when (type) {
                PostHeader::class -> PostHeaderCell(parent = parent)
                PostFooter::class -> PostFooterCell(parent = parent)
                PostText::class -> PostTextCell(parent = parent)
                PostImage::class -> PostImageCell(parent = parent)
                else -> throw IllegalArgumentException("Unhandled type $type")
            }
        }
    }
}
