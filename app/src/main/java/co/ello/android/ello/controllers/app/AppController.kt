package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class AppController(a: AppActivity) : RootController(a), StartupProtocols.Delegate, LoggedOutProtocols.Delegate, TabBarProtocols.Delegate {
    private lateinit var screen: AppProtocols.Screen

    override val containerView: ViewGroup? get() = screen.containerView

    override fun createView(): View {
        val screen = AppScreen(activity)
        this.screen = screen
        return screen.contentView
    }

    override fun onAppear() {
        super.onAppear()
        showStartup()
    }

    private fun showStartup() {
        loadStaticBadges(requestQueue)
        show(StartupController(activity, this))
    }

    override fun startupLoggedIn(credentials: Credentials) = showLoggedInScreen()
    override fun startupLoggedOut() = showLoggedOutScreen()

    override fun loggedOutDidLogin(credentials: Credentials) = showLoggedInScreen()
    override fun loggedInDidLogout() = showLoggedOutScreen()

    private fun showLoggedOutScreen() {
        val navController = NavigationController(activity)
        val loggedOutController = LoggedOutController(activity, this)
        navController.push(loggedOutController)
        show(navController)
    }

    private fun showLoggedInScreen() {
        val loggedInController = TabBarController(activity, this)
        show(loggedInController)
    }

    fun showAppSpinner() {
        screen.spinnerVisibility(true)
        stopInteraction()
    }

    fun hideAppSpinner() {
        screen.spinnerVisibility(false)
        allowInteraction()
    }
}
