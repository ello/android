package co.ello.android.ello

import android.view.View
import android.os.Handler


class StartupController(a: AppActivity) : Controller(a) {
    private val handler = Handler()
    private var screen: StartupProtocols.Screen? = null
    var delegate: StartupProtocols.Delegate? = null

    override fun createView(): View {
        val screen = StartupScreen(activity)
        this.screen = screen
        return screen.contentView
    }

    override fun onAppear() {
        val runnable = Runnable {
            delegate?.startupLoggedOut()
        }

        handler.postDelayed(runnable, 1000)
    }

}
