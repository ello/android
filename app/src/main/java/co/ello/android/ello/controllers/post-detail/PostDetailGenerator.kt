package co.ello.android.ello

import java.util.UUID
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.android.UI


class PostDetailGenerator(val delegate: PostDetailProtocols.Controller?, val queue: Queue)
    : PostDetailProtocols.Generator
{
    var streamToken: UUID? = null
    private fun newUUID(): UUID {
        return randomUUID().apply { streamToken = this }
    }

    override fun loadPostDetail(token: Token) {
        val tmpToken = ID("15914579")
        val currentToken = newUUID()
        launch(UI) exit@ {
            val detailResult = API().postDetail(tmpToken, username = null).enqueue(queue)
            val commentsResult = API().postComments(tmpToken).enqueue(queue)
            if (currentToken != streamToken)  return@exit

            when(detailResult) {
                is Success -> {
                    val items = parsePost(detailResult.value)
                    delegate?.loadedPostDetail(items)
                }
                is Failure -> {
                    val error = StreamCellItem(type = StreamCellType.Error("Could not load post"))
                    delegate?.loadedPostDetail(listOf(error))
                }
            }

            when(commentsResult) {
                is Success -> {
                    val (_, comments) = commentsResult.value
                    val items = StreamParser().parse(comments)
                    delegate?.loadedPostComments(items)
                }
                is Failure -> {
                    val error = StreamCellItem(type = StreamCellType.Error("Could not load comments"))
                    delegate?.loadedPostComments(listOf(error))
                }
            }
        }
    }

    override fun parsePost(post: Post): List<StreamCellItem> {
        return StreamParser().parse(listOf(post))
    }
}
