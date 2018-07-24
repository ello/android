package co.ello.android.ello.controllers.notifications

import co.ello.android.ello.*
import co.ello.android.ello.Queue
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*

class NotificationsGenerator(val delegate: NotificationsProtocols.Controller?, val queue: Queue)
    : NotificationsProtocols.Generator
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

    override fun loadNotifications(filter: API.NotificationFilter) {
        val currentToken = newUUID()
        launch(UI) exit@ {
            val result = API().notificationsStream(filter).enqueue(queue)
            if (currentToken != streamToken)  return@exit
            when (result) {
                is Success -> {
                    val (_, notifications) = result.value
                    val items = StreamParser().parse(notifications)
                    delegate?.loadedNotifications(items)
                }
                is Failure -> {delegate?.loadedNotifications(listOf())
                println(result.error)}
            }
        }
    }

}