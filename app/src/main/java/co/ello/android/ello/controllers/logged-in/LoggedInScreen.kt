package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView


class LoggedInScreen(
    activity: Activity,
    private val delegate: LoggedInProtocols.Controller?
    ) : LoggedInProtocols.Screen {
    override val contentView: View = activity.layoutInflater.inflate(R.layout.logged_in_layout, null)
    override val containerView: ViewGroup = contentView.findViewById(R.id.containerView)

    private val homeButton: Button = contentView.findViewById(R.id.homeButton)
    private val homeImage: ImageView = contentView.findViewById(R.id.homeImage)
    private val discoverButton: Button = contentView.findViewById(R.id.discoverButton)
    private val discoverImage: ImageView = contentView.findViewById(R.id.discoverImage)
    private val omnibarButton: Button = contentView.findViewById(R.id.omnibarButton)
    private val omnibarImage: ImageView = contentView.findViewById(R.id.omnibarImage)
    private val notificationsButton: Button = contentView.findViewById(R.id.boltButton)
    private val notificationsImage: ImageView = contentView.findViewById(R.id.boltImage)
    private val profileButton: Button = contentView.findViewById(R.id.personButton)
    private val profileImage: ImageView = contentView.findViewById(R.id.personImage)

    init {
        homeButton.setOnClickListener {
            unselectAll()
            homeImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_home_black))
            delegate?.didSelectTab(LoggedInTab.Home)
        }
        discoverButton.setOnClickListener {
            unselectAll()
            discoverImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_discover_black))
            delegate?.didSelectTab(LoggedInTab.Discover)
        }
        omnibarButton.setOnClickListener {
            unselectAll()
            omnibarImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_omni_black))
            delegate?.didSelectTab(LoggedInTab.Omnibar)
        }
        notificationsButton.setOnClickListener {
            unselectAll()
            notificationsImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_bolt_black))
            delegate?.didSelectTab(LoggedInTab.Notifications)
        }
        profileButton.setOnClickListener {
            unselectAll()
            profileImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_person_black))
            delegate?.didSelectTab(LoggedInTab.Profile)
        }

        homeImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_home_black))
    }

    private fun unselectAll() {
        homeImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_home_gray))
        discoverImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_discover_gray))
        omnibarImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_omni_gray))
        notificationsImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_bolt_gray))
        profileImage.setImageDrawable(contentView.context.getDrawable(R.drawable.tabbar_person_gray))
    }
}
