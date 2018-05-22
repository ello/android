package co.ello.android.ello

import android.support.v4.view.MotionEventCompat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout


class StreamSelectionCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.stream_selection_cell, parent, false)), View.OnTouchListener {
    private val linearLayout: LinearLayout
    private val tabs: List<UnderbarTabHolder>

    var selectedIndex: Int = 0
        set(value) {
            field = value
            updateSelected(value)
        }

    init {
        linearLayout = itemView.findViewById(R.id.linearLayout)

        val tabTitles: List<String> = listOf(
            R.string.Category_featured,
            R.string.Category_trending,
            R.string.Category_recent,
            R.string.Category_shop).map { id -> itemView.context.resources.getString(id) }
        tabs = tabTitles.map { tabTitle ->
            val tabHolder = UnderbarTabHolder(LayoutInflater.from(itemView.context).inflate(R.layout.underbar_tab_layout, linearLayout, false))
            tabHolder.blackBar.visibility = if (tabTitle == tabTitles[selectedIndex]) View.VISIBLE else View.INVISIBLE
            tabHolder.button.setText(tabTitle)
            tabHolder.button.setOnTouchListener(this)
            linearLayout.addView(tabHolder.view)
            tabHolder
        }
    }

    private fun tabSelected(selectedTab: UnderbarTabHolder) {
        val index = tabs.indexOf(selectedTab)
        updateSelected(index)
        selectedIndex = index
        streamCellItem?.extra = index
        streamController?.streamSelectionChanged(index)
    }

    private fun updateSelected(selectedIndex: Int) {
        for ((index, tabHolder) in tabs.withIndex()) {
            val visibility = if (index == selectedIndex) View.VISIBLE else View.INVISIBLE
            tabHolder.blackBar.visibility = visibility
        }
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        for ((index, tabHolder) in tabs.withIndex()) {
            if (tabHolder.button != view)  continue
            if (index == selectedIndex)  continue

            var selected = false
            val visibility = when(event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    View.VISIBLE
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_MOVE -> {
                    if (event.x < 0 || event.x > view.measuredWidth.toFloat()) View.INVISIBLE
                    else {
                        selected = event.actionMasked == MotionEvent.ACTION_UP
                        View.VISIBLE
                    }
                }
                else -> View.INVISIBLE
            }

            tabHolder.blackBar.visibility = visibility
            if (selected) {
                tabSelected(tabHolder)
            }
            break
        }
        return false
    }

}
