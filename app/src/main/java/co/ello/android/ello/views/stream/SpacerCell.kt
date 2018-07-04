package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup


class SpacerCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.spacer_cell, parent, false)) {
}
