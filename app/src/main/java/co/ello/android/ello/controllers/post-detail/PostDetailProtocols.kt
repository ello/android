package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class PostDetailProtocols {
    interface Screen {
        val contentView: View
        val streamContainer: ViewGroup
    }

    interface Controller {
        fun loadedPostDetail(post: Post)
    }

    interface Generator {
        fun loadPostDetail(token: Token)
    }
}
