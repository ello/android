package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView


class ProfileHeaderNameCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_name_cell, parent, false)) {
    val nameLabel: TextView = itemView.findViewById(R.id.nameLabel)
    val usernameLabel: TextView = itemView.findViewById(R.id.usernameLabel)

    data class Config(
        val name: String,
        val atName: String
        )

    var config: Config? = null
        set(value) {
            nameLabel.text = value?.name
            usernameLabel.text = value?.atName
        }
}
