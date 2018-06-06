package co.ello.android.ello

import android.view.ViewGroup


abstract class StreamableController(a: AppActivity) : BaseController(a) {
    abstract val viewForStream: ViewGroup

    val streamController = StreamController(activity)

    override val childControllers: Iterable<Controller> get() { return listOf(streamController) }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }

    init {
        streamController.assignParent(this, isVisible = false)
    }

    override fun onViewCreated() {
        super.onViewCreated()
        viewForStream.addView(streamController.view)
    }
}
