package co.ello.android.ello

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.net.URL


class HomeScreen : HomeProtocols.Screen {
    override var delegate: HomeProtocols.Controller? = null

    override val contentView: View
    override val containerView: ViewGroup
    private val linearLayout: ViewGroup
    private val tabHolders: List<TabHolder>

    data class TabHolder(val view: View) {
        val button = view.findViewById<Button>(R.id.button)
        val blackBar = view.findViewById<View>(R.id.blackBar)
    }

    constructor(activity: Activity, tabs: List<String>) {
        contentView = activity.layoutInflater.inflate(R.layout.home_layout, null)
        containerView = contentView.findViewById(R.id.containerView)
        linearLayout = contentView.findViewById(R.id.linearLayout)

        tabHolders = tabs.map { title ->
            val tabHolder = TabHolder(activity.layoutInflater.inflate(R.layout.home_tab_layout, linearLayout, false))
            tabHolder.blackBar.visibility = View.INVISIBLE
            tabHolder.button.setText(title)
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

    private fun tabSelected(tab: TabHolder) {
        val index = tabHolders.indexOf(tab)
        delegate?.tabSelected(index)
    }
}
