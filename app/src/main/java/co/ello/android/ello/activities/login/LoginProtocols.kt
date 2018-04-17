package co.ello.android.ello

import android.view.View
import android.view.Window


class LoginProtocols {
    interface Screen {
        val contentView: View
        var delegate: Delegate?
        fun showErrors(usernameMessage: String?, passwordMessage: String?)
        fun spinnerVisibility(visibile: Boolean, window: Window)
        fun onDestroy()
    }

    interface Delegate {
        fun submit(username: String, password: String)
        fun success(credentials: Credentials)
        fun failure()
    }

    interface Generator {
        var delegate: LoginProtocols.Delegate?
        fun login(queue: Queue, username: String, password: String)
    }
}
