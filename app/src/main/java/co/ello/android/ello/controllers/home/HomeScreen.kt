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

    private val firstBlackBar: View
    private val secondBlackBar: View
    private val thirdBlackBar: View

    enum class Tab {
        First, Second, Third
    }

    constructor(activity: Activity) {
        contentView = activity.layoutInflater.inflate(R.layout.home_layout, null)
        containerView = contentView.findViewById(R.id.containerView)

        firstBlackBar = contentView.findViewById(R.id.firstBlackBar)
        secondBlackBar = contentView.findViewById(R.id.secondBlackBar)
        thirdBlackBar = contentView.findViewById(R.id.thirdBlackBar)

        val firstButton = contentView.findViewById<Button>(R.id.firstButton)
        firstButton.setOnClickListener { tabSelected(Tab.First) }

        val secondButton = contentView.findViewById<Button>(R.id.secondButton)
        secondButton.setOnClickListener { tabSelected(Tab.Second) }
        val thirdButton = contentView.findViewById<Button>(R.id.thirdButton)
        thirdButton.setOnClickListener { tabSelected(Tab.Third) }
    }

    override fun highlight(tab: Tab) {
        firstBlackBar.visibility = View.INVISIBLE
        secondBlackBar.visibility = View.INVISIBLE
        thirdBlackBar.visibility = View.INVISIBLE

        when(tab) {
            Tab.First  -> firstBlackBar.visibility = View.VISIBLE
            Tab.Second -> secondBlackBar.visibility = View.VISIBLE
            Tab.Third  -> thirdBlackBar.visibility = View.VISIBLE
        }
    }

    private fun tabSelected(tab: Tab) {
        delegate?.tabSelected(tab)
    }
}
