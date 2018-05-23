package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager


class StreamController(a: AppActivity)
    : BaseController(a)
{
    private lateinit var screen: RecyclerView
    private var adapter: Adapter = Adapter(emptyList(), streamController = this)

    var streamSelectionDelegate: StreamSelectionDelegate? = null

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

    fun hasPlaceholderItems(placeholder: StreamCellType.PlaceholderType): Boolean {
        return adapter.items.any { it.placeholderType == placeholder }
    }

    fun replacePlaceholder(placeholder: StreamCellType.PlaceholderType, with: List<StreamCellItem>) {
        for (item in with) {
            item.placeholderType = placeholder
        }

        var didInsert = false
        val items: MutableList<StreamCellItem> = mutableListOf()
        for (item in adapter.items) {
            if (item.placeholderType == placeholder) {
                if (!didInsert) {
                    didInsert = true
                    items.addAll(with)
                }
            }
            else {
                items.add(item)
            }
        }

        if (!didInsert) {
            items.addAll(with)
        }

        replaceAll(items)
    }

    fun replaceAll(items: List<StreamCellItem>) {
        adapter = Adapter(items, streamController = this)
        if (isViewLoaded)  screen.adapter = adapter
    }

    fun streamTappedPost(cell: StreamCell, item: StreamCellItem, post: Post) {
        val postDetailController = PostDetailController(activity, post = post)
        navigationController?.push(postDetailController)
    }

    fun streamTappedUser(cell: StreamCell, item: StreamCellItem, user: User) {
    }

    fun toolbarTappedLoves(cell: StreamCell, item: StreamCellItem) {
    }

    fun toolbarTappedComments(cell: StreamCell, item: StreamCellItem) {
    }

    fun toolbarTappedRepost(cell: StreamCell, item: StreamCellItem) {
    }

    fun toolbarTappedShare(cell: StreamCell, item: StreamCellItem) {
    }

    fun streamSelectionChanged(index: Int) {
        streamSelectionDelegate?.streamSelectionChanged(index)
    }
}
