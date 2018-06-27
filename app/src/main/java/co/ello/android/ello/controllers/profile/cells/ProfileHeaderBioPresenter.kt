package co.ello.android.ello


object ProfileHeaderBioPresenter {
    fun configure(cell: ProfileHeaderBioCell, item: StreamCellItem) {
        val user = item.model as? User ?: return
        val bio = user.formattedShortBio ?: return

        cell.config(ProfileHeaderBioCell.Config(
            content = bio
            ))
    }
}
