package co.ello.android.ello.controllers.notifications

import co.ello.android.ello.*
import co.ello.android.ello.Queue
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*

/**
 * Created by sahitpenmatcha on 7/11/18.
 */
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
            val result = API().notificationsStream().enqueue(queue)
            if (currentToken != streamToken)  return@exit
            when (result) {
                is Success -> {
                    val (_, notifications) = result.value
                    //this parsing part is prob wrong
                    val items = StreamParser().parse(notifications)
                    delegate?.loadedNotifications(items)
                }
                is Failure -> println("notifications fail: ${result.error}")
            }
        }
    }

}