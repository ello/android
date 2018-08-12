package co.ello.android.ello.controllers.notifications

import android.util.Log
import android.view.View
import android.view.ViewGroup
import co.ello.android.ello.*

class NotificationsController(a: AppActivity) : StreamableController(a), NotificationsProtocols.Controller  {

    private lateinit var screen: NotificationsProtocols.Screen
    private var generator: NotificationsProtocols.Generator = NotificationsGenerator(delegate = this, queue = requestQueue)

    override val viewForStream: ViewGroup get() = screen.streamContainer

    override fun createView(): View {
        val screen = NotificationsScreen(activity, delegate = this)
        this.screen = screen
        return screen.contentView
    }

    override fun onStart() {
        streamController.replaceAll(listOf(StreamCellItem(type = StreamCellType.Spinner, placeholderType = StreamCellType.PlaceholderType.Spinner)))
        generator.loadNotifications(API.NotificationFilter.All)
    }

    override fun loadedNotifications(items: List<StreamCellItem>) {
        streamController.replaceAll(items)
    }

    override fun categorySelected(index: Int) {
        val category = when(index) {
            0 -> API.NotificationFilter.All
            1 -> API.NotificationFilter.Comments
            2 -> API.NotificationFilter.Mentions
            3 -> API.NotificationFilter.Loves
            4 -> API.NotificationFilter.Reposts
            5 -> API.NotificationFilter.Relationships
            else -> return
        }
        streamController.replaceAll(listOf(StreamCellItem(type = StreamCellType.Spinner, placeholderType = StreamCellType.PlaceholderType.Spinner)))
        generator.loadNotifications(category)
    }


}