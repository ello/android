package co.ello.android.ello

import android.view.View


class LoggedInController(
    a: AppActivity,
    val delegate: LoggedInProtocols.Delegate
    ) : BaseController(a), LoggedInProtocols.Controller
{
    private lateinit var screen: LoggedInProtocols.Screen

    private var selectedIndex = 0
    private val selectedController: BaseController get() { return childTabControllers[selectedIndex] }
    private val selectedNavigationController: NavigationController get() { return childNavControllers[selectedIndex] }
    private val childTabControllers: List<BaseController>
    private val childNavControllers: List<NavigationController>

    override val childControllers: Iterable<Controller> get() { return childTabControllers }
    override val visibleChildControllers: Iterable<Controller> get() { return listOf(childNavControllers[selectedIndex]) }
    override val firstResponder: Controller get() { return childNavControllers[selectedIndex].firstResponder }

    init {
        val homeController = HomeController(activity,
            delegate = null,
            childControllers = listOf(
                Pair("Editorial", CategoryController(activity)),
                Pair("Artist Invites", CategoryController(activity)),
                Pair("Following", CategoryController(activity))
                )
            )

        childTabControllers = listOf(
            homeController,
            CategoryController(activity),
            CategoryController(activity),
            CategoryController(activity),
            CategoryController(activity)
            )
        childNavControllers = childTabControllers.map { controller ->
            val navigationController = NavigationController(activity)
            navigationController.assignParent(this, isVisible = false)
            navigationController.push(controller)
            navigationController
        }
    }

    override fun createView(): View {
        val screen = LoggedInScreen(activity, delegate = this)
        screen.containerView.addView(selectedNavigationController.view)
        this.screen = screen
        return screen.contentView
    }

    override fun didSelectTab(tab: LoggedInTab) {
        val newSelectedIndex = when (tab) {
            LoggedInTab.Home -> 0
            LoggedInTab.Discover -> 1
            LoggedInTab.Omnibar -> 2
            LoggedInTab.Notifications -> 3
            LoggedInTab.Profile -> 4
        }
        if (newSelectedIndex == selectedIndex)  return

        screen.containerView.removeView(selectedNavigationController.view)
        selectedNavigationController.disappear()

        selectedIndex = newSelectedIndex
        screen.containerView.addView(selectedNavigationController.view)
        selectedNavigationController.appear()
    }
}
