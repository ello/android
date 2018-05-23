package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup


class EditorialsScreen : EditorialsProtocols.Screen {
    private val delegate: EditorialsProtocols.Controller?

    override val contentView: View
    override val streamContainer: ViewGroup

    constructor(activity: Activity, delegate: EditorialsProtocols.Controller?) {
        this.delegate = delegate

        contentView = activity.layoutInflater.inflate(R.layout.streamable_layout, null)
        streamContainer = contentView as ViewGroup
    }
}
