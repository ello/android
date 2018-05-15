package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class HomeProtocols {
    interface Screen {
        var delegate: Controller?
        val contentView: View
        val containerView: ViewGroup

        fun highlight(tab: HomeScreen.Tab)
    }

    interface Controller {
        fun tabSelected(tab: HomeScreen.Tab)
    }

    interface Delegate {
        fun homeTabSelected(tab: HomeScreen.Tab)
    }
}
