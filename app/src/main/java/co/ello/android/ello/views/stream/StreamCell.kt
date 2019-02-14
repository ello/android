package co.ello.android.ello

import android.view.View
import androidx.recyclerview.widget.RecyclerView


open class StreamCell(view: View) : RecyclerView.ViewHolder(view) {
    var streamController: StreamController? = null
    var streamCellItem: StreamCellItem? = null

    open fun onStart() {}
    open fun onFinish() {}
    open fun onViewRecycled() {}
}
