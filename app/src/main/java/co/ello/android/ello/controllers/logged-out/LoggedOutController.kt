package co.ello.android.ello

import android.view.View


class LoggedOutController(a: AppActivity, val delegate: LoggedOutProtocols.Delegate) : BaseController(a), LoggedOutProtocols.Controller, LoginProtocols.Delegate, JoinProtocols.Delegate {
    private lateinit var screen: LoggedOutProtocols.Screen


    override fun createView(): View {
        val screen = LoggedOutScreen(activity)
        screen.delegate = this
        this.screen = screen
        return screen.contentView
    }

    override fun onStart() {
        API().globalPostStream(API.CategoryFilter.Featured)
            .enqueue(requestQueue)
            .onSuccess { test ->
                println("globalPostStream success: $test")
            }
            .onFailure { error ->
                println("globalPostStream fail: $error")
            }
    }

    override fun showJoinScreen() {
        navigationController?.push(JoinController(activity, this))
    }

    override fun joinDidJoin(credentials: Credentials) {
        delegate.loggedOutDidLogin(credentials)
    }

    override fun joinDidCancel() {
        navigationController?.pop()
    }

    override fun showLoginScreen() {
        navigationController?.push(LoginController(activity, this))
    }

    override fun loginDidLogin(credentials: Credentials) {
        delegate.loggedOutDidLogin(credentials)
    }

    override fun loginDidCancel() {
        navigationController?.pop()
    }
}
