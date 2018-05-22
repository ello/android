package co.ello.android.ello

import android.view.View
import android.widget.Button


data class UnderbarTabHolder(val view: View) {
    val button = view.findViewById<Button>(R.id.button)
    val blackBar = view.findViewById<View>(R.id.blackBar)
}
