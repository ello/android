package co.ello.android.ello

import android.view.View


class LoggedOutController(a: AppActivity, val delegate: LoggedOutProtocols.Delegate) : BaseController(a), LoggedOutProtocols.Controller, LoginProtocols.Delegate, JoinProtocols.Delegate {
    private lateinit var screen: LoggedOutProtocols.Screen

    private val childNavController = NavigationController(activity)
    override val childControllers: Iterable<Controller> get() { return listOf(childNavController) }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }

    init {
        childNavController.assignParent(this)
    }

    override fun createView(): View {
        val screen = LoggedOutScreen(activity)
        screen.containerView.addView(childNavController.view)
        screen.delegate = this
        this.screen = screen
        return screen.contentView
    }

    override fun onStart() {
        val categoryController = CategoryController(activity)
        childNavController.push(categoryController)
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
