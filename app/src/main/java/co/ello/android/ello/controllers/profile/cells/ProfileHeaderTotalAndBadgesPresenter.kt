package co.ello.android.ello


object ProfileHeaderTotalAndBadgesPresenter {
    fun configure(cell: ProfileHeaderTotalAndBadgesCell, item: StreamCellItem) {
        val user = item.model as? User ?: return

        cell.config(ProfileHeaderTotalAndBadgesCell.Config(
            totalCount = user.totalViewsCount,
            badges = user.badges
            ))
    }
}
