package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class EditorialsController(a: AppActivity) : StreamableController(a), EditorialsProtocols.Controller {
    private lateinit var screen: EditorialsProtocols.Screen
    private var generator: EditorialsProtocols.Generator = EditorialsGenerator(delegate = this, queue = requestQueue)

    override val viewForStream: ViewGroup get() = screen.streamContainer

    override fun createView(): View {
        val screen = EditorialsScreen(activity, delegate = this)
        this.screen = screen
        return screen.contentView
    }

    override fun onStart() {
        generator.loadEditorialsStream()
    }

    override fun loadedEditorialsStream(editorials: List<Editorial>) {
        val items = StreamParser().parse(editorials)
        streamController.replaceAll(items)
    }
}
