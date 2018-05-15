package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class AppController(a: AppActivity) : RootController(a), StartupProtocols.Delegate, LoggedOutProtocols.Delegate {
    private lateinit var screen: AppProtocols.Screen

    override val containerView: ViewGroup? get() = screen.containerView

    override fun createView(): View {
        val screen = AppScreen(activity)
        this.screen = screen
        return screen.contentView
    }

    override fun onAppear() {
        showStartup()
    }

    private fun showStartup() {
        show(StartupController(activity, this))
    }

    override fun startupLoggedIn(credentials: Credentials) = loggedOutDidLogin(credentials)
    override fun startupLoggedOut() = showLoggedOutScreen()

    private fun showLoggedOutScreen() {
        val navController = NavigationController(activity)
        val loggedOutController = LoggedOutController(activity, this)
        navController.push(loggedOutController)
        show(navController)
    }

    override fun loggedOutDidLogin(credentials: Credentials) {
        println("=============== AppController.kt at line 34 ===============");
        showHomeScreen()
    }

    private fun showHomeScreen() {
        // show(HomeController(activity, this))
    }

    private fun homeDidLogout() = showLoggedOutScreen()

    fun showAppSpinner() {
        screen.spinnerVisibility(true)
        stopInteraction()
    }

    fun hideAppSpinner() {
        screen.spinnerVisibility(false)
        allowInteraction()
    }
}
