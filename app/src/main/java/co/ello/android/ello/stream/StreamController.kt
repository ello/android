package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager


class StreamController(a: AppActivity) : BaseController(a) {
    private lateinit var screen: RecyclerView

    class Adapter(val items: List<StreamCellItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        init {
            setHasStableIds(true)
        }

        override fun getItemCount(): Int = items.size

        override fun getItemId(position: Int): Long =
                items[position].uuid.hashCode().toLong()

        override fun getItemViewType(position: Int): Int =
                items[position].type.viewType

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
                StreamCellType.createViewHolder(parent = parent, viewType = viewType)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = items[position]
            item.type.bindViewHolder(holder, item = item)
        }
    }

    override fun createView(): View {
        val recycler = RecyclerView(activity)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = Adapter(emptyList())
        screen = recycler
        return screen
    }

    fun addAll(items: List<StreamCellItem>) {
        screen.adapter = Adapter(items)
    }
}
