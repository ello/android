package co.ello.android.ello

import android.view.View


class LoggedOutController(
    a: AppActivity,
    val delegate: LoggedOutProtocols.Delegate
    ) : BaseController(a), LoggedOutProtocols.Controller, LoginProtocols.Delegate, JoinProtocols.Delegate
{
    private lateinit var screen: LoggedOutProtocols.Screen

    private val discoverController = CategoryController(activity)
    private val childNavigationController = NavigationController(activity)

    override val childControllers: Iterable<Controller> get() { return listOf(childNavigationController) }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }
    override val firstResponder: Controller get() = childNavigationController.firstResponder

    init {
        childNavigationController.assignParent(this, isVisible = false)
        childNavigationController.push(discoverController)
    }

    override fun createView(): View {
        val screen = LoggedOutScreen(activity, delegate = this)
        screen.containerView.addView(childNavigationController.view)
        this.screen = screen
        return screen.contentView
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
