package co.ello.android.ello

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.android.UI


class ProfileHeaderButtonsCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_buttons_cell, parent, false)) {
    private val editButton: Button = itemView.findViewById(R.id.editButton)
    private val relationshipControl: StyledButton = itemView.findViewById(R.id.relationshipControl)
    private val hireButton: Button = itemView.findViewById(R.id.hireButton)
    private val collaborateButton: Button = itemView.findViewById(R.id.collaborateButton)
    private val mentionButton: Button = itemView.findViewById(R.id.mentionButton)
    private var nextRelationship: RelationshipPriority? = null

    data class Config(
        val isCurrentUser: Boolean,
        val showHireButton: Boolean,
        val showCollabButton: Boolean,
        val showMentionButton: Boolean,
        val relationship: RelationshipPriority
        )

    init {
        relationshipControl.setOnClickListener { relationshipControlTapped() }
    }

    fun config(value: Config) {
        editButton.visibility = if (value.isCurrentUser) View.VISIBLE else View.GONE
        relationshipControl.visibility = if (value.isCurrentUser) View.GONE else View.VISIBLE
        hireButton.visibility = if (value.showHireButton) View.VISIBLE else View.GONE
        collaborateButton.visibility = if (value.showCollabButton) View.VISIBLE else View.GONE
        mentionButton.visibility = if (value.showMentionButton) View.VISIBLE else View.GONE

        updateRelationship(value.relationship)
    }

    private fun updateRelationship(relationship: RelationshipPriority) {
        when(relationship) {
            RelationshipPriority.Following -> {
                relationshipControl.style = StyledButton.Style.GrayPill
                relationshipControl.text = T(R.string.Profile_relationship_following)
                nextRelationship = RelationshipPriority.Inactive
            }
            RelationshipPriority.Mute -> {
                relationshipControl.style = StyledButton.Style.RedPill
                relationshipControl.text = T(R.string.Profile_relationship_muted)
                nextRelationship = RelationshipPriority.Inactive
            }
            RelationshipPriority.Block -> {
                relationshipControl.style = StyledButton.Style.RedPill
                relationshipControl.text = T(R.string.Profile_relationship_blocked)
                nextRelationship = RelationshipPriority.Inactive
            }
            else -> {
                relationshipControl.style = StyledButton.Style.GreenPill
                relationshipControl.text = T(R.string.Profile_relationship_follow)
                nextRelationship = RelationshipPriority.Following
            }
        }
    }

    private fun relationshipControlTapped() {
        val streamController = streamController ?: return
        val item = streamCellItem ?: return
        val nextRelationship = nextRelationship ?: return
        val user = item.model as? User ?: return

        launch(UI) {
            val result = streamController.relationshipController.updateRelationship(user.id, next = nextRelationship)
            when (result) {
                is Success -> updateRelationship(result.value)
                is Failure -> print("error ${result.error}")
            }
        }
    }
}
