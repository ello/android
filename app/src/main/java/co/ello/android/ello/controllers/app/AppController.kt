package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout


class AppController(a: AppActivity) : Controller(a), StartupProtocols.Delegate, LoggedOutProtocols.Delegate {
    var controller: Controller? = null

    override fun createView(): View {
        val view = FrameLayout(activity)
        view.setBackgroundColor(R.color.black)
        return view
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
        val viewGroup: ViewGroup = this.view as ViewGroup
        this.controller?.let {
            it.assignParent(null)
            viewGroup.removeView(it.view)
            it.onDisappear()
        }

        controller.assignParent(this)
        viewGroup.addView(controller.view)
        controller.onAppear()

        this.controller = controller
    }

    fun showAppSpinner() {
        // spinnerContainer.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        // spinnerContainer.setFocusable(true)
        // spinnerContainer.requestFocus()
    }

    fun hideAppSpinner() {
        // spinnerContainer.visibility = View.INVISIBLE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
        // spinnerContainer.isFocusable = false
        // spinnerContainer.clearFocus()
    }
}
