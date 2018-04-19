package co.ello.android.ello

import android.view.View


class StartupProtocols {
    interface Screen {
        val contentView: View
    }

    interface Delegate {
        fun startupLoggedIn(credentials: Credentials)
        fun startupLoggedOut()
    }
}
