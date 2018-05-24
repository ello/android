package co.ello.android.ello

import android.view.View
import android.view.Window


abstract class Controller(val activity: AppActivity) {
    private var _parent: Controller? = null
    private var _isStarted = false
    private var _isVisible = false

    open val isRunning: Boolean get() {
        var ancestor = parent
        if (parent == null)  return false
        while (ancestor != null) {
            if (!ancestor.isRunning)  return false
            ancestor = ancestor.parent
        }
        return true
    }
    open val childControllers: Iterable<Controller> get() = emptyList()
    open val visibleChildControllers: Iterable<Controller> get() = emptyList()
    open val firstResponder: Controller get() = this

    val isStarted: Boolean get() { return _isStarted }
    val isVisible: Boolean get() { return _isVisible }

    val window: Window get() { return activity.window }
    val requestQueue: Queue get() { return VolleyQueue(activity) }
    val parent: Controller? get() { return _parent }
    val rootController: RootController? get() { return findParent<RootController>() }
    val navigationController: NavigationController? get() { return findParent<NavigationController>() }

    private var _view: View? = null
    val isViewLoaded: Boolean get() { return _view != null }
    val view: View get() {
        var view = _view
        if (view == null) {
            view = createView()
            onViewCreated()
            _view = view
        }
        return view
    }

    open val canGoBack: Boolean get() = firstResponder.navigationController?.canGoBack ?: false
    open fun goBack() {
        firstResponder.navigationController?.goBack()
    }

    fun removeFromParent() {
        if (isStarted) {
            if (isVisible) {
                disappear()
            }
            finish()
        }
        _parent = null
    }

    fun assignParent(parentController: Controller, isVisible: Boolean) {
        assert(_parent != null, {"$this is already a child controller on ${_parent!!}"})

        _parent = parentController

        if (parentController.isStarted) {
            this.start()
            if (isVisible && parentController.isVisible) {
                this.appear()
            }
        }
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
    open fun onViewCreated() {}

    // could be called many times, for instance in a tab controller
    open fun onAppear() {}
    open fun onRotate() {}
    open fun onDisappear() {}

    fun start() {
        if (!isRunning || isStarted)  return
        _isStarted = true
        // println("calling onStart in $this (parent: ${this.parent})")
        onStart()
        for (controller in childControllers) {
            controller.start()
        }
    }

    fun appear() {
        if (!isStarted || isVisible)  return
        if (!isViewLoaded) {
            throw IllegalArgumentException("$this View should be loaded before appear() is called")
        }
        _isVisible = true
        // println("calling onAppear in $this (parent: ${this.parent})")
        onAppear()
        for (controller in visibleChildControllers) {
            controller.appear()
        }
    }

    fun rotate() {
        if (!isVisible)  return
        // println("calling onRotate in $this (parent: ${this.parent})")
        onRotate()
        for (controller in childControllers) {
            controller.rotate()
        }
    }

    fun disappear() {
        if (!isVisible)  return
        _isVisible = false
        for (controller in visibleChildControllers) {
            controller.disappear()
        }
        // println("calling onDisappear in $this (parent: ${this.parent})")
        onDisappear()
    }

    fun finish() {
        if (!isStarted)  return
        _isStarted = false
        for (controller in childControllers) {
            controller.finish()
        }
        // println("calling onFinish in $this (parent: ${this.parent})")
        onFinish()
    }
}
