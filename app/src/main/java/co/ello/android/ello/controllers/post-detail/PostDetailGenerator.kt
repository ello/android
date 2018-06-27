package co.ello.android.ello


class PostDetailGenerator(val delegate: PostDetailProtocols.Controller?, val queue: Queue)
    : PostDetailProtocols.Generator
{

    override fun loadPostDetail(token: Token) {
        API().postDetail(token, username = null)
            .enqueue(queue)
            .onSuccess { post ->
                val items = parsePost(post)
                delegate?.loadedPostDetail(items)
            }
            .onFailure { error ->
                val error = StreamCellItem(type = StreamCellType.Error("Could not load post"))
                delegate?.loadedPostDetail(listOf(error))
            }

        API().postComments(token)
            .enqueue(queue)
            .onSuccess { (_, comments) ->
                val items = StreamParser().parse(comments)
                delegate?.loadedPostComments(items)
            }
            .onFailure { error ->
                val error = StreamCellItem(type = StreamCellType.Error("Could not load comments"))
                delegate?.loadedPostComments(listOf(error))
            }
    }

    override fun parsePost(post: Post): List<StreamCellItem> {
        return StreamParser().parse(listOf(post))
    }
}
