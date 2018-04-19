package co.ello.android.ello

import android.view.View
import android.view.Window


class LoginProtocols {
    interface Screen {
        val contentView: View
        var delegate: Controller?
        fun showErrors(usernameMessage: String?, passwordMessage: String?)
        fun spinnerVisibility(visibile: Boolean, window: Window)
        fun onDestroy()
    }

    interface Controller {
        fun submit(username: String, password: String)
        fun success(credentials: Credentials)
        fun failure()
        fun cancel()
    }

    interface Generator {
        var delegate: LoginProtocols.Controller?
        fun login(queue: Queue, username: String, password: String)
    }

    interface Delegate {
        fun loginDidLogin(credentials: Credentials)
        fun loginDidCancel()
    }
}
