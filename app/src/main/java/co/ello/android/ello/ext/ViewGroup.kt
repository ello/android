package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


val ViewGroup.childViews: Iterator<View> get() = (0 until childCount).map { getChildAt(it) }.iterator()
