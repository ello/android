package co.ello.android.ello

import java.util.*


class FollowingGenerator(val delegate: FollowingProtocols.Controller?, val queue: Queue)
    : FollowingProtocols.Generator
{
    var streamToken: UUID? = null
    private fun newUUID(): UUID {
        return randomUUID().apply { streamToken = this }
    }

    override fun loadFollowing() {
        val currentToken = newUUID()

        API().globalPostStream(API.StreamFilter.Featured)
            .enqueue(queue)
            .onSuccess exit@ { (_, posts) ->
                if (currentToken != streamToken)  return@exit
                val items = StreamParser().parse(posts)
                delegate?.loadedFollowingStream(items)
            }
            .onFailure { error ->
                println("stream fail: $error")
            }
    }
}
