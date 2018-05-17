package co.ello.android.ello

import android.view.View
import java.util.*


class NavigationController(a: AppActivity) : Controller(a) {
    private var controllers = ArrayDeque<Controller>()

    override val childControllers: Iterable<Controller> get() { return controllers }
    override val visibleChildControllers: Iterable<Controller> get() { return visibleController?.let { listOf(it) } ?: emptyList() }
    override val firstResponder: Controller get() = visibleController?.firstResponder ?: super.firstResponder

    val visibleController: Controller? get() { return controllers.peek() }
    override val canGoBack: Boolean get() = controllers.size > 1

    override fun goBack() {
        pop(animated = true)
    }

    override fun createView(): View {
        return NavigationLayout(activity)
    }

    fun push(nextController: Controller, animated: Boolean? = null) {
        val twoFrameLayout = view as NavigationLayout
        val shouldAnimate = animated ?: visibleController != null
        val prevController = this.visibleController

        rootController?.stopInteraction()
        this.controllers.push(nextController)
        twoFrameLayout.push(nextController.view, animated = shouldAnimate) { finished ->
            rootController?.allowInteraction()
            if (finished)  prevController?.disappear()
        }
        nextController.assignParent(this, isVisible = true)
    }

    fun pop(animated: Boolean? = null) {
        val twoFrameLayout = view as NavigationLayout
        val shouldAnimate = animated ?: (controllers.size > 1)
        val prevController = controllers.pop()
        val visibleController = this.visibleController

        if (visibleController != null) {
            rootController?.stopInteraction()
            twoFrameLayout.pop(visibleController.view, animated = shouldAnimate) { _ ->
                rootController?.allowInteraction()
                prevController?.removeFromParent()
            }
            visibleController.appear()
        }
        else {
            prevController?.removeFromParent()
        }
    }
}
