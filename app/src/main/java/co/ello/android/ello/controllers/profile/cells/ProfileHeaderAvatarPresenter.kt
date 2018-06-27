package co.ello.android.ello


object ProfileHeaderAvatarPresenter {
    fun configure(cell: ProfileHeaderAvatarCell, item: StreamCellItem) {
        val user = item.model as? User ?: return

        val viewsAdultContent: Boolean? = null // currentUser?.viewsAdultContent
        val config = ProfileHeaderAvatarCell.Config()
        user.avatarURL(viewsAdultContent = viewsAdultContent, animated = true)?.let { url ->
            config.avatarURL = url
        }
        user.coverImageURL(viewsAdultContent = viewsAdultContent, animated = true)?.let { url ->
            config.coverImageURL = url
        }
        cell.config = config
    }
}
