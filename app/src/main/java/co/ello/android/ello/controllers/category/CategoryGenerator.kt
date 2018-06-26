package co.ello.android.ello

import java.util.*


class CategoryGenerator(val delegate: CategoryProtocols.Controller?, val queue: Queue)
    : CategoryProtocols.Generator
{
    sealed class Stream {
        object All : Stream()
        object Subscribed : Stream()
        data class Category(val id: Token) : Stream()

        override fun equals(other: Any?): Boolean {
            if (other == null)  return false
            if (other !is Stream)  return false
            if (other is All && this is All)  return true
            if (other is Subscribed && this is Subscribed)  return true
            if (other is Category && this is Category)  return other.id == this.id
            return false
        }
    }

    var streamToken: UUID? = null
    private fun newUUID(): UUID {
        return randomUUID().apply { streamToken = this }
    }

    override fun loadSubscribedCategories() {
        API().subscribedCategories()
            .enqueue(queue)
            .onSuccess { subscribedCategories ->
                delegate?.loadedSubscribedCategories(subscribedCategories)
            }
            .onFailure { error ->
                println("subscribedCategories fail: $error")
            }
    }

    override fun loadStream(filter: API.StreamFilter, stream: Stream) {
        val currentToken = newUUID()

        API().let { when(stream) {
            is Stream.All        -> it.globalPostStream(filter)
            is Stream.Subscribed -> it.subscribedPostStream(filter)
            is Stream.Category   -> it.categoryPostStream(filter, category = stream.id)
        } }.enqueue(queue)
            .onSuccess exit@ { (_, posts) ->
                if (currentToken != streamToken)  return@exit
                val items = StreamParser().parse(posts)
                delegate?.loadedCategoryStream(items)
            }
            .onFailure { error ->
                println("stream fail: $error")
            }
    }
}
