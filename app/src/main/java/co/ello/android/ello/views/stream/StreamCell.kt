package co.ello.android.ello

import android.support.v7.widget.RecyclerView
import android.view.View


open class StreamCell(view: View) : RecyclerView.ViewHolder(view) {
    var streamController: StreamController? = null
    var streamCellItem: StreamCellItem? = null

    fun onViewRecycled() {}
}
