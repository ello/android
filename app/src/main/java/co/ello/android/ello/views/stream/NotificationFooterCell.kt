package co.ello.android.ello


import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class NotificationFooterCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.notification_footer_cell, parent, false)) {
    private val timestampLabel: TextView = itemView.findViewById(R.id.timestampLabel)
    private val actionableLayout : LinearLayout = itemView.findViewById(R.id.actionableLayout)

    data class Config(
            val postActionable: Boolean,
            val postedAt: Date
    )

    fun config(value: Config) {
        if (!value.postActionable) {
            actionableLayout.visibility = View.GONE
        }
        timestampLabel.text = value.postedAt.timeAgo()
    }
}
