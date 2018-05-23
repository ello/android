package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class HomeScreen : HomeProtocols.Screen {
    private val delegate: HomeProtocols.Controller?

    override val contentView: View
    override val containerView: ViewGroup
    private val linearLayout: ViewGroup
    private val tabHolders: List<UnderbarTabHolder>

    constructor(activity: Activity, tabs: List<String>, delegate: HomeProtocols.Controller?) {
        this.delegate = delegate

        contentView = activity.layoutInflater.inflate(R.layout.home_layout, null)
        containerView = contentView.findViewById(R.id.containerView)
        linearLayout = contentView.findViewById(R.id.linearLayout)
        tabHolders = tabs.map { tabTitle ->
            val tabHolder = UnderbarTabHolder(activity.layoutInflater.inflate(R.layout.underbar_tab_layout, linearLayout, false))
            tabHolder.blackBar.visibility = View.INVISIBLE
            tabHolder.button.setText(tabTitle)
            tabHolder.button.setOnClickListener { tabSelected(tabHolder) }
            linearLayout.addView(tabHolder.view)
            tabHolder
        }
    }

    override fun highlight(tab: Int) {
        for ((index, tabHolder) in tabHolders.withIndex()) {
            tabHolder.blackBar.visibility = if (index == tab) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun tabSelected(tab: UnderbarTabHolder) {
        val index = tabHolders.indexOf(tab)
        delegate?.tabSelected(index)
    }
}
