package co.ello.android.ello

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class ProfileHeaderButtonsCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_buttons_cell, parent, false)) {
    val editButton: Button
    val relationshipControl: Button
    val hireButton: Button
    val collaborateButton: Button
    val mentionButton: Button

    data class Config(
        val isCurrentUser: Boolean,
        val showHireButton: Boolean,
        val showCollabButton: Boolean,
        val showMentionButton: Boolean
        )

    init {
        editButton = itemView.findViewById(R.id.editButton)
        relationshipControl = itemView.findViewById(R.id.relationshipControl)
        hireButton = itemView.findViewById(R.id.hireButton)
        collaborateButton = itemView.findViewById(R.id.collaborateButton)
        mentionButton = itemView.findViewById(R.id.mentionButton)
    }

    var config: Config? = null
        set(value) {
            editButton.visibility = if (value?.isCurrentUser == true) View.VISIBLE else View.GONE
            relationshipControl.visibility = if (value?.isCurrentUser == true) View.GONE else View.VISIBLE
            hireButton.visibility = if (value?.showHireButton == true) View.VISIBLE else View.GONE
            collaborateButton.visibility = if (value?.showCollabButton == true) View.VISIBLE else View.GONE
            mentionButton.visibility = if (value?.showMentionButton == true) View.VISIBLE else View.GONE
        }
}
