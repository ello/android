package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class LoggedInProtocols {
    interface Screen {
        val contentView: View
        val containerView: ViewGroup
    }

    interface Controller {
        fun didSelectTab(tab: LoggedInTab)
    }

    interface Delegate {
        fun loggedInDidLogout()
    }
}
