package co.ello.android.ello.controllers.notifications

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import co.ello.android.ello.API
import co.ello.android.ello.CategoryScreen
import co.ello.android.ello.StreamCellItem

class NotificationsProtocols {
    interface Screen {
        val contentView: View
        val streamContainer: ViewGroup
        val tabs: List<(View)>
        fun highlightSelectedTab(index: Int)
    }

    interface Controller {
        fun loadedNotifications(items: List<StreamCellItem>)
        fun categorySelected(index: Int)
    }

    interface Generator {
        fun loadNotifications(filter: API.NotificationFilter)
    }
}