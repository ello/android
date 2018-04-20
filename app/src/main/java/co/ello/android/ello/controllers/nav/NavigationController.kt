package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.util.*


open class NavigationController(a: AppActivity) : Controller(a) {
    private var controllers = ArrayDeque<Controller>()

    override val childControllers: Iterable<Controller> get() { return controllers }
    override val visibleChildControllers: Iterable<Controller> get() { return visibleController?.let { listOf(it) } ?: emptyList() }
    val visibleController: Controller? get() { return controllers.peek() }

    override fun createView(): View {
        return FrameLayout(activity)
    }

    open fun containerView(): ViewGroup {
        return view as ViewGroup
    }

    fun push(nextController: Controller) {
        val viewGroup = containerView()
        this.visibleController?.let {
            viewGroup.removeView(it.view)
            it.disappear()
        }

        this.controllers.push(nextController)
        viewGroup.addView(nextController.view)
        nextController.assignParent(this)
        nextController.start()
        nextController.appear()
    }

    fun pop() {
        val viewGroup = containerView()
        val topController = controllers.pop()
        topController?.let {
            viewGroup.removeView(it.view)
            it.assignParent(null)
            it.disappear()
            it.finish()
        }

        val nextController = visibleController
        nextController?.let {
            viewGroup.addView(it.view)
            it.appear()
        }
    }
}
