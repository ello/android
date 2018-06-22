package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView


class ProfileHeaderNameCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_name_cell, parent, false)) {
    val nameLabel: TextView
    val usernameLabel: TextView

    data class Config(
        val name: String,
        val atName: String
        )

    init {
        nameLabel = itemView.findViewById(R.id.nameLabel)
        usernameLabel = itemView.findViewById(R.id.usernameLabel)
    }

    var config: Config? = null
        set(value) {
            nameLabel.text = value?.name
            usernameLabel.text = value?.atName
        }
}
