package co.ello.android.ello

import android.view.ViewGroup


abstract class RootController(a: AppActivity) : Controller(a) {
    private var rootController: Controller? = null

    override val isRunning: Boolean get() { return true }
    override val childControllers: Iterable<Controller> get() { return rootController?.let { listOf(it) } ?: emptyList() }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }

    abstract val containerView: ViewGroup?

    fun show(controller: Controller) {
        val containerView = this.containerView ?: return

        this.rootController?.let {
            containerView.removeView(it.view)
            it.assignParent(null)
        }

        containerView.addView(controller.view)
        controller.assignParent(this)
        this.rootController = controller
    }
}
