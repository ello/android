package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


class AppController(a: AppActivity) : Controller(a), StartupProtocols.Delegate, LoginProtocols.Delegate {
    var controller: Controller? = null

    override fun createView(): View {
        return FrameLayout(activity)
    }

    override fun onAppear() {
        showStartup()
    }

    private fun showStartup() {
        val controller = StartupController(activity)
        controller.delegate = this
        show(controller)
    }

    override fun startupLoggedIn(credentials: Credentials) = didLogin(credentials)
    override fun startupLoggedOut() = showLoginScreen()

    fun showLoginScreen() {
        val controller = LoginController(activity)
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
        val frameLayout: ViewGroup = this.view as ViewGroup
        this.controller?.let {
            frameLayout.removeView(it.view)
            it.onDisappear()
        }

        frameLayout.addView(controller.view)
        controller.onAppear()
        this.controller = controller
    }
}
