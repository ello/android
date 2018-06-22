package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup


class ProfileScreen : ProfileProtocols.Screen {
    private val delegate: ProfileProtocols.Controller?

    override val contentView: View
    override val streamContainer: ViewGroup

    constructor(activity: Activity, delegate: ProfileProtocols.Controller?) {
        this.delegate = delegate

        contentView = activity.layoutInflater.inflate(R.layout.streamable_layout, null)
        streamContainer = contentView as ViewGroup
    }
}
