package co.ello.android.ello.controllers.notifications

import android.util.Log
import co.ello.android.ello.*
import co.ello.android.ello.Queue
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*

class NotificationsGenerator(val delegate: NotificationsProtocols.Controller?, val queue: Queue)
    : NotificationsProtocols.Generator
{

    var streamToken: UUID? = null
    private fun newUUID(): UUID {
        return randomUUID().apply { streamToken = this }
    }

    override fun loadNotifications() {
        val currentToken = newUUID()
        launch(UI) exit@ {
            val result = API().notificationsStream(API.NotificationFilter.create("ALL") as API.NotificationFilter).enqueue(queue)
            if (currentToken != streamToken)  return@exit
            when (result) {
                is Success -> {
                    Log.d("NOTIFICATIONSYEA", "notifications sucess")
                    val (_, notifications) = result.value
                    val items = StreamParser().parse(notifications)
                    delegate?.loadedNotifications(items)
                }
                is Failure -> Log.d("NOTIFICATIONSYEA", "notifications fail: ${result.error}")
            }
        }
    }

}