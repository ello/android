package co.ello.android.ello

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.otto.Subscribe
import java.net.URL


class StreamController(a: AppActivity)
    : BaseController(a)
{
    private lateinit var screen: RecyclerView
    private val adapter: Adapter = Adapter(emptyList(), streamController = this)

    var streamSelectionDelegate: StreamSelectionDelegate? = null

    class Adapter(var items: List<StreamCellItem>, val streamController: StreamController) : RecyclerView.Adapter<StreamCell>() {
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
            item.isBinding = true
            item.type.bindViewHolder(holder, item = item)
            item.isBinding = false
        }

        override fun onViewAttachedToWindow(holder: StreamCell) {
            holder.onStart()
        }

        override fun onViewDetachedFromWindow(holder: StreamCell) {
            holder.onFinish()
        }

        override fun onViewRecycled(holder: StreamCell) {
            holder.onViewRecycled()
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

    override fun onAppear() {
        super.onAppear()
        if (screen.adapter == null) {
            screen.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        App.eventBus.register(this)
    }

    override fun onFinish() {
        super.onFinish()
        for (i in 0.until(adapter.itemCount)) {
            val cell = screen.findViewHolderForAdapterPosition(i) as? StreamCell ?: continue
            cell.onFinish()
        }
        App.eventBus.unregister(this)
    }

    @Subscribe
    fun modelChanged(event: ModelChanged) {
        for ((index, item) in adapter.items.withIndex()) {
            val model = item.model ?: continue
            val identifier = model.identifier ?: continue
            if (event.type != identifier.table || event.id != identifier.id)  continue
            adapter.notifyItemChanged(index)
            model.update(event.property, event.value)
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

    fun resizedCell() {
        adapter.notifyDataSetChanged()
    }

    fun replaceAll(items: List<StreamCellItem>) {
        adapter.items = items
        adapter.notifyDataSetChanged()
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

    fun streamTappedURL(url: URL) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        App.startActivity(intent)
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
