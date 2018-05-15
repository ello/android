package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class HomeController(
        a: AppActivity,
        val delegate: HomeProtocols.Delegate,
        private val embeddedController: Controller,
        private val highlighted: HomeScreen.Tab
) : BaseController(a), HomeProtocols.Controller {
    private lateinit var screen: HomeProtocols.Screen

    override val childControllers: Iterable<Controller> get() { return listOf(embeddedController) }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }

    override fun createView(): View {
        val screen = HomeScreen(activity)
        screen.delegate = this
        screen.highlight(highlighted)
        this.screen = screen

        screen.containerView.addView(embeddedController.view)
        embeddedController.assignParent(this, isVisible = true)

        return screen.contentView
    }

    override fun onStart() {
    }

    override fun tabSelected(tab: HomeScreen.Tab) {
        delegate?.homeTabSelected(tab)
    }
}
