package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager


class StreamController(a: AppActivity)
    : BaseController(a), PostToolbarResponder
{
    private lateinit var screen: RecyclerView
    private var adapter: Adapter = Adapter(emptyList(), streamController = this)

    class Adapter(val items: List<StreamCellItem>, val streamController: StreamController) : RecyclerView.Adapter<StreamCell>() {
        init {
            setHasStableIds(true)
        }

        override fun getItemCount(): Int = items.size

        override fun getItemId(position: Int): Long =
                items[position].uuid.hashCode().toLong()

        override fun getItemViewType(position: Int): Int =
                items[position].type.viewType

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamCell =
                StreamCellType.createViewHolder(parent = parent, viewType = viewType)

        override fun onBindViewHolder(holder: StreamCell, position: Int) {
            val item = items[position]
            holder.streamController = streamController
            item.type.bindViewHolder(holder, item = item)
        }
    }

    override fun createView(): View {
        val recycler = RecyclerView(activity)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = adapter
        screen = recycler
        return screen
    }

    fun addAll(items: List<StreamCellItem>) {
        adapter = Adapter(items, streamController = this)
        if (isViewLoaded)  screen.adapter = adapter
    }

    override fun toolbarTappedViews(cell: StreamCell, item: StreamCellItem) {
    }

    override fun toolbarTappedLoves(cell: StreamCell, item: StreamCellItem) {
    }

    override fun toolbarTappedComments(cell: StreamCell, item: StreamCellItem) {
    }

    override fun toolbarTappedRepost(cell: StreamCell, item: StreamCellItem) {
    }

    override fun toolbarTappedShare(cell: StreamCell, item: StreamCellItem) {
    }
}
