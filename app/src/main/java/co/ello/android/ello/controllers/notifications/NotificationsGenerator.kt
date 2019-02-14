package co.ello.android.ello.controllers.notifications

import co.ello.android.ello.*
import co.ello.android.ello.Queue
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch
import java.util.*

class NotificationsGenerator(val delegate: NotificationsProtocols.Controller?, val queue: Queue)
    : NotificationsProtocols.Generator
{
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
                is Failure -> println(result.error)
            }
        }
    }
}