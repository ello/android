package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class TabBarProtocols {
    interface Screen {
        val contentView: View
        val containerView: ViewGroup
    }

    interface Controller {
        fun didSelectTab(tab: TabBarTab)
    }

    interface Delegate {
        fun loggedInDidLogout()
    }
}
