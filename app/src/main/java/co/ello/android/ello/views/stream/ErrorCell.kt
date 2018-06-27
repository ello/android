package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView


class ErrorCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.error_cell, parent, false)) {
    val messageLabel: TextView = itemView.findViewById(R.id.messageLabel)

    data class Config(
        val message: String
        )

    fun config(value: Config) {
        messageLabel.setText(value.message)
    }
}
