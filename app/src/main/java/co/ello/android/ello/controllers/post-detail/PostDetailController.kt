package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class PostDetailController(a: AppActivity) : StreamableController(a), PostDetailProtocols.Controller {
    private lateinit var screen: PostDetailProtocols.Screen
    private var generator: PostDetailProtocols.Generator = PostDetailGenerator(delegate = this, queue = requestQueue)

    override val viewForStream: ViewGroup get() = screen.streamContainer
    private lateinit var token: Token
    private var post: Post? = null

    constructor(a: AppActivity, token: Token) : this(a) {
        this.token = token
    }

    constructor(a: AppActivity, post: Post) : this(a) {
        this.token = ID(post.id)
        this.post = post
    }

    fun isShowing(post: Post): Boolean {
        return token.matches(post.id, post.token)
    }

    override fun createView(): View {
        val screen = PostDetailScreen(activity, delegate = this)
        this.screen = screen
        return screen.contentView
    }

    override fun onStart() {
        val post = this.post
        if (post == null) {
            generator.loadPostDetail(token)
        }
        else {
            loadedPostDetail(post)
        }
    }

    override fun loadedPostDetail(post: Post) {
        val items = StreamParser().parse(listOf(post))
        streamController.replaceAll(items)
    }
}
