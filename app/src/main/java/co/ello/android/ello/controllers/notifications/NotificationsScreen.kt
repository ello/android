package co.ello.android.ello.controllers.notifications

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import co.ello.android.ello.*

class NotificationsScreen : NotificationsProtocols.Screen {

    private val delegate: NotificationsProtocols.Controller?

    override val contentView: View
    override val streamContainer: ViewGroup
    override val tabs: List<(View)>

    constructor(activity: Activity, delegate: NotificationsProtocols.Controller?) {
        this.delegate = delegate

        contentView = activity.layoutInflater.inflate(R.layout.notification_layout, null)
        streamContainer = contentView.findViewById(R.id.streamContainer)

        tabs = listOf(
                contentView.findViewById<Button>(R.id.all),
                contentView.findViewById<ImageButton>(R.id.comments),
                contentView.findViewById<Button>(R.id.mentions),
                contentView.findViewById<ImageButton>(R.id.loves),
                contentView.findViewById<ImageButton>(R.id.reposts),
                contentView.findViewById<ImageButton>(R.id.relationships))

        tabs.forEachIndexed { i, tab ->
            tab.setOnClickListener {tabTapped(i)}
        }
    }

    override fun highlightSelectedTab(index : Int) {
        tabs.forEachIndexed { i, tab ->
            if (index == i) {
                tab.setBackgroundResource(R.color.black)
            }
            else {
                tab.setBackgroundResource(R.color.greyE5)
            }
        }
    }

    private fun tabTapped(index : Int) {
        highlightSelectedTab(index)
        delegate?.categorySelected(index)
    }
}