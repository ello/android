package co.ello.android.ello

import android.content.res.Resources


val Int.dpf: Float get() = this.toFloat().dpf
val Float.dpf: Float get() = (this * Resources.getSystem().displayMetrics.density)

val Int.dp: Int get() = this.toFloat().dp
val Float.dp: Int get() = this.dpf.toInt()

val Int.fontDp: Float get() = this.toFloat().fontDp
val Float.fontDp: Float get() = (this * 1.2f)
