package co.ello.android.ello


class PostDetailGenerator(val delegate: PostDetailProtocols.Controller?, val queue: Queue)
    : PostDetailProtocols.Generator
{

    override fun loadPostDetail(token: Token) {
        API().postDetail(token, username = null)
            .enqueue(queue)
            .onSuccess { post ->
                delegate?.loadedPostDetail(post)
            }
            // .onFailure { error ->
            // }
    }
}
