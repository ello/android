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

    var config: Config? = null
        set(value) {
            if (value == null)  return
            postToolbar.viewsCount = value.views
            postToolbar.lovesCount = value.loves
            postToolbar.commentsCount = value.comments
            postToolbar.repostCount = value.reposts
            postToolbar.shareVisible = value.shareable
        }

    fun viewsButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return
        val post = item.model as? Post ?: return

        streamController.streamTappedPost(post)
    }

    fun lovesButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return

        streamController.toolbarTappedLoves(cell = this, item = item)
    }

    fun commentsButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return

        streamController.toolbarTappedComments(cell = this, item = item)
    }

    fun repostButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return

        streamController.toolbarTappedRepost(cell = this, item = item)
    }

    fun shareButtonTapped() {
        val item = streamCellItem ?: return
        val streamController = streamController ?: return

        streamController.toolbarTappedShare(cell = this, item = item)
    }
}
