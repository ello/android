package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


class AppController(a: AppActivity) : Controller(a), StartupProtocols.Delegate, LoggedOutProtocols.Delegate {
    var controller: Controller? = null

    override fun createView(): View {
        return FrameLayout(activity)
    }

    override fun onAppear() {
        showStartup()
    }

    private fun showStartup() {
        show(StartupController(activity, this))
    }

    override fun startupLoggedIn(credentials: Credentials) = loggedOutDidLogin(credentials)
    override fun startupLoggedOut() = showLoggedOutScreen()

    fun showLoggedOutScreen() {
        val navController = NavigationController(activity)
        val loggedOutController = LoggedOutController(activity, this)
        navController.push(loggedOutController)
        show(navController)
    }

    override fun loggedOutDidLogin(credentials: Credentials) {
        println("=============== AppController.kt at line 34 ===============");
        // showHomeScreen()
    }

    private fun showHomeScreen() {
        // show(HomeController)
    }

    private fun show(controller: Controller) {
        val viewGroup: ViewGroup = this.view as ViewGroup
        this.controller?.let {
            it.parent = null
            viewGroup.removeView(it.view)
            it.onDisappear()
        }

        controller.parent = this
        viewGroup.addView(controller.view)
        controller.onAppear()

        this.controller = controller
    }
}
