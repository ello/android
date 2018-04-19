package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.util.*


open class NavigationController(a: AppActivity) : Controller(a) {
    private var controllers = ArrayDeque<Controller>()

    val childControllers: Iterable<Controller> get() { return controllers }
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
            it.onDisappear()
        }

        nextController.assignParent(this)
        this.controllers.push(nextController)
        viewGroup.addView(nextController.view)
        nextController.onAppear()
    }

    fun pop() {
        val viewGroup = containerView()
        val topController = controllers.pop()
        topController?.let {
            it.assignParent(null)
            viewGroup.removeView(it.view)
            it.onDisappear()
        }

        val nextController = visibleController
        nextController?.let {
            viewGroup.addView(it.view)
            it.onAppear()
        }
    }
}
