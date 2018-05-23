package co.ello.android.ello

import android.view.View
import android.view.Window


class LoginProtocols {
    interface Screen {
        val contentView: View
        var interactive: Boolean
        fun showErrors(usernameMessage: String?, passwordMessage: String?)
    }

    interface Controller {
        fun submit(username: String, password: String)
        fun success(credentials: Credentials)
        fun failure()
        fun cancel()
    }

    interface Generator {
        fun login(queue: Queue, username: String, password: String)
    }

    interface Delegate {
        fun loginDidLogin(credentials: Credentials)
        fun loginDidCancel()
    }
}
