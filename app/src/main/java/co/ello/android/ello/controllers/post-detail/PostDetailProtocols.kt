package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class PostDetailProtocols {
    interface Screen {
        val contentView: View
        val streamContainer: ViewGroup
    }

    interface Controller {
        fun loadedPostDetail(items: List<StreamCellItem>)
        fun loadedPostComments(items: List<StreamCellItem>)
    }

    interface Generator {
        fun loadPostDetail(token: Token)
        fun parsePost(post: Post): List<StreamCellItem>
    }
}
