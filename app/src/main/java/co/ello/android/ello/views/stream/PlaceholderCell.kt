package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup


class PlaceholderCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.placeholder_cell, parent, false)) {
}
