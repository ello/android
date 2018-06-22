package co.ello.android.ello


object ProfileHeaderButtonsPresenter {
    fun configure(cell: ProfileHeaderButtonsCell, item: StreamCellItem) {
        val user = item.model as? User ?: return

        cell.config = ProfileHeaderButtonsCell.Config(
            isCurrentUser = false,
            showHireButton = user.isHireable,
            showCollabButton = user.isCollaborateable,
            showMentionButton = !(user.isHireable || user.isCollaborateable)
            )
    }
}
