package co.ello.android.ello

import android.view.View


class LoginController(a: AppActivity, val delegate: LoginProtocols.Delegate) : Controller(a), LoginProtocols.Controller {
    private var screen: LoginProtocols.Screen? = null
    private val generator: LoginProtocols.Generator = LoginGenerator()

    override fun createView(): View {
        val screen = LoginScreen(activity)
        screen.delegate = this
        generator.delegate = this
        this.screen = screen
        return screen.contentView
    }

    override fun submit(username: String, password: String) {
        var usernameMessage: String? = null
        if (username == "") {
            usernameMessage = "Username is required"
        }

        var passwordMessage: String? = null
        if (password == "") {
            passwordMessage = "Password is required"
        }
        else if (password.length < 8) {
            passwordMessage = "Password must be at least 8 characters"
        }

        val screen = this.screen!!
        screen.showErrors(usernameMessage, passwordMessage)
        if (usernameMessage == null && passwordMessage == null) {
            screen.spinnerVisibility(true, window)

            generator.login(requestQueue, username, password)
        }
    }

    override fun cancel() {
        delegate.loginDidCancel()
    }

    override fun success(credentials: Credentials) {
        delegate.loginDidLogin(credentials)
    }

    override fun failure() {
        screen?.spinnerVisibility(false, window)
    }

    override fun onDestroy() {
        screen?.onDestroy()
        super.onDestroy()
    }
}
