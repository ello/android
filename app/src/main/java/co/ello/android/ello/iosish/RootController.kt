package co.ello.android.ello

import android.content.Context
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


abstract class RootController(a: AppActivity) : Controller(a) {
    private var childController: Controller? = null

    override val isRunning: Boolean get() { return true }
    override val childControllers: Iterable<Controller> get() { return childController?.let { listOf(it) } ?: emptyList() }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }

    abstract val containerView: ViewGroup?

    fun show(controller: Controller) {
        val containerView = this.containerView ?: return

        this.childController?.let {
            containerView.removeView(it.view)
            it.removeFromParent()
        }

        containerView.addView(controller.view)
        controller.assignParent(this, isVisible = true)
        this.childController = controller
    }

    fun stopInteraction() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)

        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun allowInteraction() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
    }
}
