package co.ello.android.ello

import android.view.View


class JoinController(a: AppActivity, val delegate: JoinProtocols.Delegate) : BaseController(a), JoinProtocols.Controller {
    private lateinit var screen: JoinProtocols.Screen
    private var generator: JoinProtocols.Generator = JoinGenerator(delegate = this)

    override fun createView(): View {
        val screen = JoinScreen(activity)
        screen.delegate = this
        this.screen = screen
        return screen.contentView
    }

    override fun submit(email: String, username: String, password: String) {
        var emailMessage: String? = null
        if (email.isEmpty()) {
            emailMessage = T(R.string.Error_emailRequired)
        }

        var usernameMessage: String? = null
        if (username.isEmpty()) {
            usernameMessage = T(R.string.Error_usernameRequired)
        }

        var passwordMessage: String? = null
        if (password.isEmpty()) {
            passwordMessage = T(R.string.Error_passwordRequired)
        }
        else if (password.length < 8) {
            passwordMessage = T(R.string.Error_passwordLength)
        }

        screen.showErrors(emailMessage, usernameMessage, passwordMessage)
        if (emailMessage == null && usernameMessage == null && passwordMessage == null) {
            showSpinner()
            screen.interactive = false

            generator.join(requestQueue, email, username, password)
        }
    }

    override fun cancel() {
        delegate.joinDidCancel()
    }

    override fun success(credentials: Credentials) {
        delegate.joinDidJoin(credentials)
    }

    override fun failure() {
        hideSpinner()
        screen.interactive = true
    }
}
