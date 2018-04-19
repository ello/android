package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class LoggedOutProtocols {
    interface Screen {
        val contentView: View
        val containerView: ViewGroup
    }

    interface Delegate {
        fun loggedOutDidLogin(credentials: Credentials)
    }
}
