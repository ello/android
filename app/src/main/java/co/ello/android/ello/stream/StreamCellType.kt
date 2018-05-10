package co.ello.android.ello

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup


sealed class StreamCellType {
    object PostHeader : StreamCellType()
    object PostFooter : StreamCellType()

    val viewType: Int get() = StreamCellType::class.nestedClasses.indexOf(this::class)

    fun bindViewHolder(viewHolder: RecyclerView.ViewHolder, item: StreamCellItem) =
        when(this) {
            is PostHeader -> PostHeaderPresenter.configure(viewHolder as PostHeaderCell, item)
            is PostFooter -> PostFooterPresenter.configure(viewHolder as PostFooterCell, item)
        }

    companion object {
        fun createViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val type = StreamCellType::class.nestedClasses.elementAt(viewType)
            return when (type) {
                PostHeader::class -> PostHeaderCell(parent = parent)
                PostFooter::class -> PostFooterCell(parent = parent)
                else -> throw IllegalArgumentException("Unhandled type $type")
            }
        }
    }

}
