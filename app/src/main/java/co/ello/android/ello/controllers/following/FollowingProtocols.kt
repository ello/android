package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class FollowingProtocols {
    interface Controller {
        fun loadedFollowingStream(items: List<StreamCellItem>)
    }

    interface Generator {
        fun loadFollowing()
    }
}
