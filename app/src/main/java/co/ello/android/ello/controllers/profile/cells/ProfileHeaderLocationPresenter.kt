package co.ello.android.ello


object ProfileHeaderLocationPresenter {
    fun configure(cell: ProfileHeaderLocationCell, item: StreamCellItem) {
        val user = item.model as? User ?: return
        val location = user.location ?: return

        cell.config(ProfileHeaderLocationCell.Config(
            location = location
            ))
    }
}
