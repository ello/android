package co.ello.android.ello.controllers.notifications

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import co.ello.android.ello.R

/**
 * Created by sahitpenmatcha on 7/11/18.
 */
class NotificationsScreen : NotificationsProtocols.Screen {

    private val delegate: NotificationsProtocols.Controller?

    override val contentView: View
    override val streamContainer: ViewGroup

    constructor(activity: Activity, delegate: NotificationsProtocols.Controller?) {
        this.delegate = delegate

        contentView = activity.layoutInflater.inflate(R.layout.streamable_layout, null)
        streamContainer = contentView as ViewGroup
    }
}