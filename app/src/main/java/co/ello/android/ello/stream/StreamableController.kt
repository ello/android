package co.ello.android.ello

import android.view.ViewGroup


abstract class StreamableController(a: AppActivity) : BaseController(a) {
    abstract val viewForStream: ViewGroup

    val streamController = StreamController(activity)

    override val childControllers: Iterable<Controller> get() { return listOf(streamController) }
    override val visibleChildControllers: Iterable<Controller> get() { return childControllers }

    override fun onViewCreated() {
        viewForStream.addView(streamController.view)
    }
}
