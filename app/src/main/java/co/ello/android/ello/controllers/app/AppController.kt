package co.ello.android.ello

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


class AppController(a: AppActivity) : Controller(a), StartupProtocols.Delegate, LoggedOutProtocols.Delegate {
    private var rootController: Controller? = null
    private var screen: AppProtocols.Screen? = null

    override val isRunning: Boolean get() { return true }
    override val childControllers: Iterable<Controller> get() { return rootController?.let { listOf(it) } ?: emptyList() }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }

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
        // show(HomeController(activity, this))
    }

    private fun homeDidLogout() = showLoggedOutScreen()

    private fun show(controller: Controller) {
        val containerView = this.screen?.containerView
        if (containerView == null)  return

        this.rootController?.let {
            containerView.removeView(it.view)
            it.assignParent(null)
            it.disappear()
            it.finish()
        }

        containerView.addView(controller.view)
        controller.assignParent(this)
        controller.start()
        controller.appear()

        this.rootController = controller
    }

    fun showAppSpinner() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)

        screen?.spinnerVisibility(true)
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun hideAppSpinner() {
        screen?.spinnerVisibility(false)
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
    }
}
