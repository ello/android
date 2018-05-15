package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class LoggedOutProtocols {
    interface Screen {
        var delegate: Controller?
        val contentView: View
        val containerView: ViewGroup
    }

    interface Controller {
        fun showLoginScreen()
        fun showJoinScreen()
    }

    interface Delegate {
        fun loggedOutDidLogin(credentials: Credentials)
    }
}
