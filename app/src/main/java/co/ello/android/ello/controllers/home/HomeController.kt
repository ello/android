package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class HomeController : BaseController, HomeProtocols.Controller {
    private val delegate: HomeProtocols.Delegate?
    private lateinit var screen: HomeProtocols.Screen

    private val controllersTitles: List<String>
    private val controllers: List<Controller>
    private var selectedIndex: Int
    private val selectedController: Controller get() = controllers[selectedIndex]

    override val childControllers: Iterable<Controller> get() { return controllers }
    override val visibleChildControllers: Iterable<Controller> get() { return listOf(selectedController) }
    override val firstResponder: Controller get() = selectedController.firstResponder

    constructor(
        activity: AppActivity,
        delegate: HomeProtocols.Delegate?,
        childControllers: List<Pair<String, Controller>>,
        selected: Int
        ) : super(activity) {
        this.delegate = delegate
        this.controllersTitles = childControllers.map { it.first }
        this.controllers = childControllers.map { it.second }
        this.selectedIndex = selected
    }

    override fun createView(): View {
        val screen = HomeScreen(activity, tabs = controllersTitles, delegate = this)
        screen.highlight(selectedIndex)
        screen.containerView.addView(selectedController.view)
        this.screen = screen

        return screen.contentView
    }

    override fun onStart() {
        for ((index, controller) in controllers.withIndex()) {
            controller.assignParent(this, isVisible = index == selectedIndex)
        }
    }

    override fun tabSelected(tab: Int) {
        if (selectedIndex == tab)  return

        screen.containerView.removeView(selectedController.view)
        selectedController.disappear()

        selectedIndex = tab
        screen.highlight(tab)
        screen.containerView.addView(selectedController.view)
        selectedController.appear()

        delegate?.homeTabSelected(tab)
    }
}
