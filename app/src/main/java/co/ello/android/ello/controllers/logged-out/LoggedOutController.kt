package co.ello.android.ello

import android.view.View


class LoggedOutController(
    a: AppActivity,
    val delegate: LoggedOutProtocols.Delegate
    ) : BaseController(a), LoggedOutProtocols.Controller, LoginProtocols.Delegate, JoinProtocols.Delegate
{
    private lateinit var screen: LoggedOutProtocols.Screen

    private val discoverController = CategoryController(activity)

    override val childControllers: Iterable<Controller> get() { return listOf(discoverController) }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }
    override val firstResponder: Controller get() = discoverController.firstResponder

    override fun createView(): View {
        val screen = LoggedOutScreen(activity)
        screen.containerView.addView(discoverController.view)
        screen.delegate = this
        this.screen = screen
        return screen.contentView
    }

    override fun onStart() {
        discoverController.assignParent(this, isVisible = true)
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
