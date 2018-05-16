package co.ello.android.ello

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.net.URL


class EditorialsScreen : EditorialsProtocols.Screen {
    override var delegate: EditorialsProtocols.Controller? = null

    override val contentView: View
    override val streamContainer: ViewGroup

    constructor(activity: Activity) {
        contentView = activity.layoutInflater.inflate(R.layout.streamable_layout, null)
        streamContainer = contentView as ViewGroup
    }
}
