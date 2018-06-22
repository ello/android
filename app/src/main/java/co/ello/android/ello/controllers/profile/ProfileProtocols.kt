package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class ProfileProtocols {
    interface Screen {
        val contentView: View
        val streamContainer: ViewGroup
    }

    interface Controller {
        fun loadedUser(user: User)
    }

    interface Generator {
        fun loadUser(token: Token)
    }
}
