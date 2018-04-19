package co.ello.android.ello

import android.view.View
import android.view.Window


abstract class Controller(val activity: AppActivity) {
    val window: Window get() { return activity.window }
    val requestQueue: VolleyQueue get() { return VolleyQueue(activity) }

    var parent: Controller? = null
    val navigationController: NavigationController? get() {
        var parent: Controller? = this
        while (parent != null) {
            if (parent is NavigationController)  return parent
            parent = parent.parent
        }
        return null
    }

    private var _view: View? = null
    val isViewLoaded: Boolean get() { return _view != null }
    val view: View get() {
        val view = _view ?: createView()
        if (_view == null) {
            _view = view
        }
        return view
    }

    abstract fun createView(): View
    open fun onDestroy() {}

    open fun onAppear() {}
    open fun onDisappear() {}
}
