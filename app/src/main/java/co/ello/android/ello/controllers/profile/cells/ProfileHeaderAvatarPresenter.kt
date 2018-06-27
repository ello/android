package co.ello.android.ello


object ProfileHeaderAvatarPresenter {
    fun configure(cell: ProfileHeaderAvatarCell, item: StreamCellItem) {
        val user = item.model as? User ?: return

        val viewsAdultContent: Boolean? = null // currentUser?.viewsAdultContent
        cell.config(ProfileHeaderAvatarCell.Config(
            avatarURL = user.avatarURL(viewsAdultContent = viewsAdultContent, animated = true),
            coverImageURL = user.coverImageURL(viewsAdultContent = viewsAdultContent, animated = true)
            ))
    }
}
