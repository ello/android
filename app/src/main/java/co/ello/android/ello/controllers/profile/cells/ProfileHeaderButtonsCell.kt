package co.ello.android.ello

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class ProfileHeaderButtonsCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_buttons_cell, parent, false)) {
    val editButton: Button = itemView.findViewById(R.id.editButton)
    val relationshipControl: Button = itemView.findViewById(R.id.relationshipControl)
    val hireButton: Button = itemView.findViewById(R.id.hireButton)
    val collaborateButton: Button = itemView.findViewById(R.id.collaborateButton)
    val mentionButton: Button = itemView.findViewById(R.id.mentionButton)

    data class Config(
        val isCurrentUser: Boolean,
        val showHireButton: Boolean,
        val showCollabButton: Boolean,
        val showMentionButton: Boolean
        )

    fun config(value: Config) {
        editButton.visibility = if (value.isCurrentUser) View.VISIBLE else View.GONE
        relationshipControl.visibility = if (value.isCurrentUser) View.GONE else View.VISIBLE
        hireButton.visibility = if (value.showHireButton) View.VISIBLE else View.GONE
        collaborateButton.visibility = if (value.showCollabButton) View.VISIBLE else View.GONE
        mentionButton.visibility = if (value.showMentionButton) View.VISIBLE else View.GONE
    }
}
