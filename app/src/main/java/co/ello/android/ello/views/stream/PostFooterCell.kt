package co.ello.android.ello

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import java.net.URL


class PostFooterCell(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_footer_cell, parent, false)) {

    data class Config(
        val views: Count,
        val comments: Count,
        val loves: Count,
        val reposts: Count,
        val shareable: Boolean
        )

    init {
    }

    var config: Config? = null
        set(value) {
        }
}
