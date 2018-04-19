package co.ello.android.ello

import android.view.View
import android.view.Window


class JoinProtocols {
    interface Screen {
        val contentView: View
        var delegate: Controller?
        var interactive: Boolean
        fun showErrors(emailMessage: String?, usernameMessage: String?, passwordMessage: String?)
        fun onDestroy()
    }

    interface Controller {
        fun submit(email: String, username: String, password: String)
        fun success(credentials: Credentials)
        fun failure()
        fun cancel()
    }

    interface Generator {
        var delegate: Controller?
        fun join(queue: Queue, email: String, username: String, password: String)
    }

    interface Delegate {
        fun joinDidJoin(credentials: Credentials)
        fun joinDidCancel()
    }
}
