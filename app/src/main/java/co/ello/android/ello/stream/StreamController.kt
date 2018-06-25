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

    override fun onRotate() {
        for (item in adapter.items) {
            item.height = null
        }
        replaceAll(adapter.items)
    }

    override fun createView(): View {
        val screen = RecyclerView(activity)
        screen.layoutManager = LinearLayoutManager(activity)
        this.screen = screen
        return screen
    }

    override fun onStart() {
        if (screen.adapter == null) {
            screen.adapter = adapter
        }
    }

    fun hasPlaceholderItems(placeholder: StreamCellType.PlaceholderType): Boolean {
        return adapter.items.any {
            it.placeholderType == placeholder && it.type != StreamCellType.Placeholder
        }
    }

    fun replacePlaceholder(placeholder: StreamCellType.PlaceholderType, with: List<StreamCellItem>) {
        if (with.isEmpty()) {
            replacePlaceholder(placeholder, listOf(StreamCellItem(type = StreamCellType.Placeholder)))
            return
        }

        for (item in with) {
            item.placeholderType = placeholder
        }

        var didInsert = false
        val newItems: MutableList<StreamCellItem> = mutableListOf()
        for (item in adapter.items) {
            if (item.placeholderType == placeholder && !didInsert) {
                didInsert = true
                newItems.addAll(with)
            }
            else if (item.placeholderType != placeholder) {
                newItems.add(item)
            }
        }

        if (!didInsert) {
            newItems.addAll(with)
        }

        replaceAll(newItems)
    }

    fun replaceAll(items: List<StreamCellItem>) {
        adapter = Adapter(items, streamController = this)
        if (isViewLoaded)  screen.adapter = adapter
    }

    fun streamTappedPost(post: Post) {
        val parentController = findParent<PostDetailController>()
        if (parentController != null && parentController.isShowing(post))  return

        val postDetailController = PostDetailController(activity, post = post)
        navigationController?.push(postDetailController)
    }

    fun streamTappedUser(user: User) {
        val parentController = findParent<ProfileController>()
        if (parentController != null && parentController.isShowing(user))  return

        val profileController = ProfileController(activity, user = user)
        navigationController?.push(profileController)
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
