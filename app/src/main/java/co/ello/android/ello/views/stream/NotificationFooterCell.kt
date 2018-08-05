package co.ello.android.ello


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

class NotificationFooterCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.notification_footer_cell, parent, false)) {
    private val timestampLabel: TextView = itemView.findViewById(R.id.timestampLabel)

    data class Config(
            val postedAt: Date
    )

    fun config(value: Config) {
        timestampLabel.text = value.postedAt.timeAgo()
    }
}
