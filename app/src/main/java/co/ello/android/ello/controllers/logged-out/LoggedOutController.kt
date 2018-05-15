package co.ello.android.ello

import android.view.View


class LoggedOutController(
    a: AppActivity,
    val delegate: LoggedOutProtocols.Delegate
    ) : BaseController(a), LoggedOutProtocols.Controller, LoginProtocols.Delegate, JoinProtocols.Delegate, HomeProtocols.Delegate
{
    private lateinit var screen: LoggedOutProtocols.Screen

    private val editorialsNavController = NavigationController(activity)
    private val artistInvitesNavController = NavigationController(activity)
    private val discoverNavController = NavigationController(activity)
    private var selectedNavController = discoverNavController

    override val childControllers: Iterable<Controller> get() { return listOf(editorialsNavController, artistInvitesNavController, discoverNavController) }
    override val visibleChildControllers: Iterable<Controller> get() { return listOf(selectedNavController) }

    override fun createView(): View {
        val screen = LoggedOutScreen(activity)
        screen.containerView.addView(selectedNavController.view)
        screen.delegate = this
        this.screen = screen
        return screen.contentView
    }

    override fun onViewCreated() {
        super.onViewCreated()

        editorialsNavController.assignParent(this, isVisible = selectedNavController == editorialsNavController)
        artistInvitesNavController.assignParent(this, isVisible = selectedNavController == artistInvitesNavController)
        discoverNavController.assignParent(this, isVisible = selectedNavController == discoverNavController)
    }

    override fun onStart() {
        editorialsNavController.push(HomeController(activity,
                delegate = this,
                embeddedController = CategoryController(activity),
                highlighted = HomeScreen.Tab.First))

        artistInvitesNavController.push(HomeController(activity,
                delegate = this,
                embeddedController = CategoryController(activity),
                highlighted = HomeScreen.Tab.Second))

        discoverNavController.push(HomeController(activity,
                delegate = this,
                embeddedController = CategoryController(activity),
                highlighted = HomeScreen.Tab.Third))
    }

    override fun homeTabSelected(tab: HomeScreen.Tab) {
        when(tab) {
            HomeScreen.Tab.First -> show(editorialsNavController)
            HomeScreen.Tab.Second -> show(artistInvitesNavController)
            HomeScreen.Tab.Third -> show(discoverNavController)
        }
    }

    private fun show(controller: NavigationController) {
        if (selectedNavController == controller)  return

        screen.containerView.removeView(selectedNavController.view)
        selectedNavController.disappear()

        selectedNavController = controller
        screen.containerView.addView(controller.view)
        controller.appear()
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
