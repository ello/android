package co.ello.android.ello


object ProfileHeaderStatsPresenter {
    fun configure(cell: ProfileHeaderStatsCell, item: StreamCellItem) {
        val user = item.model as? User ?: return

        val followersCount: String
        val followingCount: String
        val lovesCount: String
        if (user.username == "ello") {
            followersCount = "∞"
            followingCount = "∞"
            lovesCount = "∞"
        }
        else {
            followersCount = user.followersCount?.let { it.numberToHuman() } ?: ""
            followingCount = user.followingCount?.let { it.numberToHuman() } ?: ""
            lovesCount = user.lovesCount?.let { it.numberToHuman() } ?: ""
        }
        cell.config(ProfileHeaderStatsCell.Config(
            postsCount = user.postsCount?.let { it.numberToHuman() } ?: "0",
            followingCount = followersCount,
            followersCount = followingCount,
            lovesCount = lovesCount
            ))
    }
}
