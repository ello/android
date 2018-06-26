package co.ello.android.ello

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


class FollowingController(a: AppActivity) : StreamableController(a), FollowingProtocols.Controller {
    private var generator: FollowingProtocols.Generator = FollowingGenerator(delegate = this, queue = requestQueue)

    override val viewForStream: ViewGroup get() { return view as ViewGroup }

    override fun createView(): View {
        val view = FrameLayout(activity)
        view.setBackgroundColor(activity.getColor(R.color.white))
        return view
    }

    override fun onViewCreated() {
        super.onViewCreated()
        streamController.replaceAll(listOf(StreamCellItem(type = StreamCellType.Spinner)))
    }

    override fun onStart() {
        super.onStart()
        generator.loadFollowing()
    }

    override fun loadedFollowingStream(items: List<StreamCellItem>) {
        streamController.replaceAll(items)
    }
}
