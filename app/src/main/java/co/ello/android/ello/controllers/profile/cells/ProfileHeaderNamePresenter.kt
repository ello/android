package co.ello.android.ello


object ProfileHeaderNamePresenter {
    fun configure(cell: ProfileHeaderNameCell, item: StreamCellItem) {
        val user = item.model as? User ?: return

        cell.config = ProfileHeaderNameCell.Config(
            name = user.name,
            atName = user.atName
            )
    }
}
