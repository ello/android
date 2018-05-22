package co.ello.android.ello

import java.util.*


class CategoryGenerator(val delegate: CategoryProtocols.Controller?, val queue: Queue)
    : CategoryProtocols.Generator
{
    sealed class Stream {
        object All : Stream()
        object Subscribed : Stream()
        data class Category(val id: Token) : Stream()
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

    override fun loadStream(stream: Stream) {
        val currentToken = newUUID()

        API().let { when(stream) {
            is Stream.All        -> it.globalPostStream(API.CategoryFilter.Featured)
            is Stream.Subscribed -> it.subscribedPostStream(API.CategoryFilter.Featured)
            is Stream.Category   -> it.categoryPostStream(API.CategoryFilter.Featured, category = stream.id)
        } }.enqueue(queue)
            .onSuccess exit@ { (_, posts) ->
                if (currentToken != streamToken)  return@exit
                delegate?.loadedCategoryStream(posts)
            }
            .onFailure { error ->
                println("stream fail: $error")
            }
    }
}
