package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


class LoggedOutController(a: AppActivity, val delegate: LoggedOutProtocols.Delegate) : Controller(a), LoginProtocols.Delegate {
    private var screen: LoggedOutProtocols.Screen? = null
    var childController: Controller? = null

    override fun createView(): View {
        val screen = LoggedOutScreen(activity)
        //screen.delegate = this
        this.screen = screen
        return screen.contentView
    }

    override fun onAppear() {
        showLoginScreen()
    }

    fun showLoginScreen() {
        navigationController?.push(LoginController(activity, this))
    }

    override fun loginDidLogin(credentials: Credentials) {
        delegate.loggedOutDidLogin(credentials)
    }

    override fun loginDidCancel() {
        navigationController?.pop()
    }
}
