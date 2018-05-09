package co.ello.android.ello

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View


sealed class StreamCellType {
    object PostHeader : StreamCellType()

    val viewType: Int get() = StreamCellType::class.nestedClasses.indexOf(this::class)

    fun bindViewHolder(viewHolder: RecyclerView.ViewHolder, item: StreamCellItem) {
        when(this) {
            is PostHeader -> {
                val holder = viewHolder as ViewHolder
                holder.label.setText("Testing @ ${item.model}")
            }
        }
    }

    class ViewHolder(val label: StyledLabel) : RecyclerView.ViewHolder(label)

    companion object {
        fun createViewHolder(context: Context, viewType: Int): RecyclerView.ViewHolder {
            val type = StreamCellType::class.nestedClasses.elementAtOrNull(viewType)
            return when(type) {
                PostHeader::class -> ViewHolder(label = StyledLabel(context))
                else -> throw IllegalArgumentException("Unhandled type $type")
            }
        }
    }

}
