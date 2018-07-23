package co.ello.android.ello.controllers.notifications

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
        generator.loadNotifications()
    }

    override fun loadedNotifications(items: List<StreamCellItem>) {
        streamController.replaceAll(items)
    }


}