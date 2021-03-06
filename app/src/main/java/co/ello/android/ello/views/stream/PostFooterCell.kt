package co.ello.android.ello

import android.view.LayoutInflater
import android.view.ViewGroup


class PostFooterCell(parent: ViewGroup) : StreamCell(LayoutInflater.from(parent.context).inflate(R.layout.post_footer_cell, parent, false)) {
    private val postToolbar: PostToolbar

    data class Config(
        val views: Count,
        val loves: Count,
        val comments: Count,
        val reposts: Count,
        val shareable: Boolean
        )

    init {
        postToolbar = itemView as PostToolbar

        postToolbar.setViewsButtonListener { viewsButtonTapped() }
        postToolbar.setLovesButtonListener { lovesButtonTapped() }
        postToolbar.setCommentsButtonListener { commentsButtonTapped() }
        postToolbar.setRepostButtonListener { repostButtonTapped() }
        postToolbar.setShareButtonListener { shareButtonTapped() }
    }

    fun config(value: Config) {
        postToolbar.viewsCount = value.views
        postToolbar.lovesCount = value.loves
        postToolbar.commentsCount = value.comments
        postToolbar.repostCount = value.reposts
        postToolbar.shareVisible = value.shareable
    }

    fun viewsButtonTapped() {
        val streamController = streamController ?: return
        val item = streamCellItem ?: return
        val post = item.model as? Post ?: return

        streamController.streamTappedPost(post)
    }

    fun lovesButtonTapped() {
        val streamController = streamController ?: return
        val item = streamCellItem ?: return

        streamController.toolbarTappedLoves(cell = this, item = item)
    }

    fun commentsButtonTapped() {
        val streamController = streamController ?: return
        val item = streamCellItem ?: return

        streamController.toolbarTappedComments(cell = this, item = item)
    }

    fun repostButtonTapped() {
        val streamController = streamController ?: return
        val item = streamCellItem ?: return

        streamController.toolbarTappedRepost(cell = this, item = item)
    }

    fun shareButtonTapped() {
        val streamController = streamController ?: return
        val item = streamCellItem ?: return

        streamController.toolbarTappedShare(cell = this, item = item)
    }
}
