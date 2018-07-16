package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView


class ProfileHeaderLocationCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.profile_header_location_cell, parent, false)) {
    val locationLabel: TextView = itemView.findViewById(R.id.locationLabel)

    data class Config(
        val location: String
        )

    fun config(value: Config) {
        locationLabel.text = value.location
    }
}
