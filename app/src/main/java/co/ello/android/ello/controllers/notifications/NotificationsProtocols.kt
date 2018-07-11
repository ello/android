package co.ello.android.ello.controllers.notifications

import android.view.View
import android.view.ViewGroup
import co.ello.android.ello.StreamCellItem

/**
 * Created by sahitpenmatcha on 7/11/18.
 */
class NotificationsProtocols {
    interface Screen {
        val contentView: View
        val streamContainer: ViewGroup
    }

    interface Controller {
        fun loadedNotifications(items: List<StreamCellItem>)
    }

    interface Generator {
        fun loadNotifications()
    }
}