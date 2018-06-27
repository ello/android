package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView


class ProfileHeaderStatsCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_stats_cell, parent, false)) {
    val postsCountLabel: TextView = itemView.findViewById(R.id.postsCountLabel)
    val followingCountLabel: TextView = itemView.findViewById(R.id.followingCountLabel)
    val followersCountLabel: TextView = itemView.findViewById(R.id.followersCountLabel)
    val lovesCountLabel: TextView = itemView.findViewById(R.id.lovesCountLabel)

    data class Config(
        val postsCount: String,
        val followingCount: String,
        val followersCount: String,
        val lovesCount: String
        )

    fun config(value: Config) {
        postsCountLabel.text = value.postsCount
        followingCountLabel.text = value.followingCount
        followersCountLabel.text = value.followersCount
        lovesCountLabel.text = value.lovesCount
    }
}
