package co.ello.android.ello

import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater


class AppScreen : AppProtocols.Screen {
    override val contentView : ConstraintLayout

    constructor(activity: Activity) {
        val inflater = activity.layoutInflater
        contentView = inflater.inflate(R.layout.app_layout, null) as ConstraintLayout
    }

}
