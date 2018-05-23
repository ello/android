package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class HomeProtocols {
    interface Screen {
        val contentView: View
        val containerView: ViewGroup

        fun highlight(tab: Int)
    }

    interface Controller {
        fun tabSelected(tab: Int)
    }

    interface Delegate {
        fun homeTabSelected(tab: Int)
    }
}
