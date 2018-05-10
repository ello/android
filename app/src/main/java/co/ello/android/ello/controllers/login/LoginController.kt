package co.ello.android.ello

import android.view.View


class LoginController(a: AppActivity, val delegate: LoginProtocols.Delegate) : BaseController(a), LoginProtocols.Controller {
    private lateinit var screen: LoginProtocols.Screen
    private val generator: LoginProtocols.Generator = LoginGenerator(delegate = this)

    override fun createView(): View {
        val screen = LoginScreen(activity)
        screen.delegate = this
        this.screen = screen
        return screen.contentView
    }

    override fun submit(username: String, password: String) {
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

        screen.showErrors(usernameMessage, passwordMessage)
        if (usernameMessage == null && passwordMessage == null) {
            showSpinner()
            screen.interactive = false

            generator.login(requestQueue, username, password)
        }
    }

    override fun cancel() {
        delegate.loginDidCancel()
    }

    override fun success(credentials: Credentials) {
        AuthToken.update(credentials)
        delegate.loginDidLogin(credentials)
    }

    override fun failure() {
        hideSpinner()
        screen.interactive = true
    }
}
