package co.ello.android.ello

import java.util.UUID
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class FollowingGenerator(val delegate: FollowingProtocols.Controller?, val queue: Queue)
    : FollowingProtocols.Generator
{
    var streamToken: UUID? = null
    private fun newUUID(): UUID {
        return randomUUID().apply { streamToken = this }
    }

    override fun loadFollowing() {
        val currentToken = newUUID()
        launch(UI) exit@ {
            val result = API().followingPostStream().enqueue(queue)
            if (currentToken != streamToken)  return@exit
            when(result) {
                is Success -> {
                    val (_, posts) = result.value
                    val items = StreamParser().parse(posts)
                    delegate?.loadedFollowingStream(items)
                }
                is Failure -> {
                    val error = StreamCellItem(type = StreamCellType.Error("Could not load following stream"))
                    delegate?.loadedFollowingStream(listOf(error))
                }
            }
        }
    }
}
