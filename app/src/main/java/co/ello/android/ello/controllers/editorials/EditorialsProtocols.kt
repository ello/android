package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class EditorialsProtocols {
    interface Screen {
        val contentView: View
        val streamContainer: ViewGroup
    }

    interface Controller {
        fun loadedEditorialsStream(editorials: List<Editorial>)
    }

    interface Generator {
        fun loadEditorialsStream(queue: Queue)
    }
}
