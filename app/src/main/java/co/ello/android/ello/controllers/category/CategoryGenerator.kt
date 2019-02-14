package co.ello.android.ello

import java.util.UUID
import kotlinx.coroutines.launch
import kotlinx.coroutines.android.UI


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
        launch(UI) {
            val result = API().subscribedCategories().enqueue(queue)
            when (result) {
                is Success -> delegate?.loadedSubscribedCategories(result.value)
                is Failure -> println("subscribedCategories fail: ${result.error}")
            }
        }
    }

    override fun loadStream(filter: API.StreamFilter, stream: Stream) {
        val currentToken = newUUID()
        launch(UI) exit@ {
            var result = API().let { when(stream) {
                    is Stream.All        -> it.globalPostStream(filter)
                    is Stream.Subscribed -> it.subscribedPostStream(filter)
                    is Stream.Category   -> it.categoryPostStream(filter, category = stream.id)
                } }.enqueue(queue)
            if (currentToken != streamToken)  return@exit
            when (result) {
                is Success -> {
                    val (_, posts) = result.value
                    val items = StreamParser().parse(posts)
                    delegate?.loadedCategoryStream(items)
                }
                is Failure -> println("stream fail: ${result.error}")
            }
        }
    }
}
