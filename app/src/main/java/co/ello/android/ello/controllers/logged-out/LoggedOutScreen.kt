package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


class LoggedOutScreen : LoggedOutProtocols.Screen {
    override val contentView: View
    override val containerView: ViewGroup

    constructor(activity: Activity) {
        // val inflater = activity.layoutInflater
        // contentView = inflater.inflate(R.layout.logged_out_layout, null)
        contentView = FrameLayout(activity)
        containerView = contentView
    }

}
