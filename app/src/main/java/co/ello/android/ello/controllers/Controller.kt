package co.ello.android.ello

import android.view.View
import android.view.Window


abstract class Controller(val activity: AppActivity) {
    val window: Window get() { return activity.window }
    val requestQueue: VolleyQueue get() { return VolleyQueue(activity) }

    private var _parent: Controller? = null
    val parent: Controller? get() { return _parent }
    open val isRunning: Boolean get() {
        var ancestor = parent
        if (parent == null)  return false
        while (ancestor != null) {
            if (!ancestor.isRunning)  return false
            ancestor = ancestor.parent
        }
        return true
    }
    open val childControllers: Iterable<Controller> get() { return emptyList() }
    open val visibleChildControllers: Iterable<Controller> get() { return emptyList() }

    private var _isStarted = false
    val isStarted: Boolean get() { return _isStarted }

    private var _isVisible = false
    val isVisible: Boolean get() { return _isVisible }

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
        assert(_parent != null, {"$this is already a child controller on ${_parent!!}"})
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

    // called when the view is added to a controller (i.e. 'parent' is set) for
    // the first time. onFinish is called when the parent is set to null.
    // Usually these will only be called once per controller, but if a
    // controller is *moved* to another controller, these would be called again.
    open fun onStart() {}
    open fun onFinish() {}

    // could be called many times, for instance in a tab controller
    open fun onAppear() {}
    open fun onDisappear() {}

    open fun start() {
        if (!isRunning || isStarted)  return
        _isStarted = true
        onStart()
        for (controller in childControllers) {
            controller.start()
        }
    }

    open fun appear() {
        if (!isStarted || isVisible)  return
        _isVisible = true
        onAppear()
        for (controller in visibleChildControllers) {
            controller.appear()
        }
    }

    open fun disappear() {
        if (!isVisible)  return
        _isVisible = false
        for (controller in visibleChildControllers) {
            controller.disappear()
        }
        onDisappear()
    }

    open fun finish() {
        if (!isStarted)  return
        _isStarted = false
        for (controller in childControllers) {
            controller.finish()
        }
        onFinish()
    }

    fun showSpinner() {
        appController?.showAppSpinner()
    }

    fun hideSpinner() {
        appController?.hideAppSpinner()
    }
}
