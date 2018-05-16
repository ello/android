package co.ello.android.ello

import android.view.View


class LoggedOutController(
    a: AppActivity,
    val delegate: LoggedOutProtocols.Delegate
    ) : BaseController(a), LoggedOutProtocols.Controller, LoginProtocols.Delegate, JoinProtocols.Delegate, HomeProtocols.Delegate
{
    private lateinit var screen: LoggedOutProtocols.Screen

    private val editorialsController = EditorialsController(activity)
    private val artistInvitesController = CategoryController(activity)
    private val discoverController = CategoryController(activity)
    private val homeController: HomeController

    init {
        homeController = HomeController(activity, delegate = this,
            childControllers = listOf(
                Pair(T(R.string.Editorials_title), editorialsController),
                Pair(T(R.string.ArtistInvites_title), artistInvitesController),
                Pair(T(R.string.Discover_title), discoverController)
                ),
            selected = 2)
     }

    override val childControllers: Iterable<Controller> get() { return listOf(homeController) }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }

    override fun createView(): View {
        val screen = LoggedOutScreen(activity)
        screen.containerView.addView(homeController.view)
        screen.delegate = this
        this.screen = screen
        return screen.contentView
    }

    override fun homeTabSelected(tab: Int) {
    }

    override fun onStart() {
        homeController.assignParent(this, isVisible = true)
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
