package co.ello.android.ello

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class AppActivity : AppCompatActivity(), StartupProtocols.Delegate, LoginProtocols.Delegate {
    private var controller: Controller? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        showStartupScreen()
    }

    fun showStartupScreen() {
        val controller = StartupController(this)
        controller.delegate = this
        show(controller)
    }

    override fun startupLoggedIn(credentials: Credentials) = didLogin(credentials)
    override fun startupLoggedOut() = showLoginScreen()

    fun showLoginScreen() {
        val controller = LoginController(this)
        controller.delegate = this
        show(controller)
    }

    override fun didLogin(credentials: Credentials) {
        // show()
    }

    fun showLoggedInScreen() {
        // show(LoggedInController)
    }

    private fun show(controller: Controller) {
        this.controller?.onDisappear()
        setContentView(controller.createView())
        controller.onAppear()

        this.controller = controller
    }
}
