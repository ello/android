package co.ello.android.ello

import android.view.View
import android.view.Window


abstract class Controller(val activity: AppActivity) {
    val window: Window get() { return activity.window }
    val requestQueue: VolleyQueue get() { return VolleyQueue(activity) }

    private var _parent: Controller? = null
    val parent: Controller? get() { return _parent }

    val appController: AppController? get() { return findParent<AppController>() }
    val navigationController: NavigationController? get() { return findParent<NavigationController>() }

    private var _view: View? = null
    val isViewLoaded: Boolean get() { return _view != null }
    val view: View get() {
        val view = _view ?: createView()
        if (_view == null) {
            _view = view
        }
        return view
    }

    fun assignParent(controller: Controller?) {
        _parent = controller
    }

    inline fun <reified T : Controller> findParent(): T? {
        var controller: Controller? = this
        while (controller != null) {
            if (controller is T)  return controller
            controller = controller.parent
        }
        return null
    }

    abstract fun createView(): View
    open fun onDestroy() {}

    open fun onAppear() {}
    open fun onDisappear() {}

    fun showSpinner() {
        appController?.showAppSpinner()
    }

    fun hideSpinner() {
        appController?.hideAppSpinner()
    }
}
