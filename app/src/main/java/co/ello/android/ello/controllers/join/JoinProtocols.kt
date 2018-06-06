package co.ello.android.ello

import android.view.View


class JoinProtocols {
    interface Screen {
        val contentView: View
        var interactive: Boolean
        fun showErrors(emailMessage: String?, usernameMessage: String?, passwordMessage: String?)
    }

    interface Controller {
        fun submit(email: String, username: String, password: String)
        fun success(credentials: Credentials)
        fun failure()
        fun cancel()
    }

    interface Generator {
        fun join(email: String, username: String, password: String)
    }

    interface Delegate {
        fun joinDidJoin(credentials: Credentials)
        fun joinDidCancel()
    }
}
