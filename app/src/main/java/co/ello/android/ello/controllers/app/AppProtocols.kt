package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class AppProtocols {
    interface Screen {
        val contentView: View
        val containerView: ViewGroup
        fun spinnerVisibility(visibile: Boolean)
    }
}
