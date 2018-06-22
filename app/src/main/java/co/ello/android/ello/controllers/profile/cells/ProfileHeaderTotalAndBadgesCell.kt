package co.ello.android.ello

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


class ProfileHeaderTotalAndBadgesCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_total_and_badges_cell, parent, false)) {
    val totalCountContainer: View
    val badgesContainer: View
    val badgesLinearContainer: LinearLayout
    val separator: View
    val totalCountLabel: TextView

    data class Config(
        val totalCount: Int?,
        val badges: List<Badge>
        )

    init {
        totalCountContainer = itemView.findViewById(R.id.totalCountContainer)
        badgesContainer = itemView.findViewById(R.id.badgesContainer)
        badgesLinearContainer = itemView.findViewById(R.id.badgesLinearContainer)
        separator = itemView.findViewById(R.id.separator)
        totalCountLabel = itemView.findViewById(R.id.totalCountLabel)
    }

    var config: Config? = null
        set(value) {
            separator.visibility = View.VISIBLE
            totalCountContainer.visibility = View.VISIBLE
            badgesContainer.visibility = View.VISIBLE

            val totalCount = value?.totalCount
            if (totalCount != null && totalCount > 0) {
                totalCountLabel.text = totalCount.let { it.numberToHuman() } ?: ""
            }
            else {
                totalCountContainer.visibility = View.GONE
                separator.visibility = View.GONE
            }

            val badges = value?.badges
            if (badges != null && badges.isNotEmpty()) {
                badgesLinearContainer.removeAllViews()
                val limitedBadges: List<Badge>
                if (badges.size > 3) {
                    limitedBadges = badges.subList(0, 3)
                }
                else {
                    limitedBadges = badges
                }
                limitedBadges.forEach { badge ->
                    val badgeImageView = ImageView(itemView.context)
                    val params = LinearLayout.LayoutParams(20.dp, 20.dp)
                    params.marginStart = 2.5f.dp
                    params.marginEnd = 2.5f.dp
                    badgeImageView.layoutParams = params
                    badgeImageView.setImageURL(badge.imageURL)
                    badgesLinearContainer.addView(badgeImageView)
                }
            }
            else {
                badgesContainer.visibility = View.GONE
                separator.visibility = View.GONE
            }
        }
}
