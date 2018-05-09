package co.ello.android.ello

import android.os.Handler
import android.view.View


class StartupController(a: AppActivity, val delegate: StartupProtocols.Delegate) : BaseController(a) {
    private val handler = Handler()
    private lateinit var screen: StartupProtocols.Screen

    override fun createView(): View {
        val screen = StartupScreen(activity)
        this.screen = screen
        return screen.contentView
    }

    override fun onAppear() {
        val runnable = Runnable {
            delegate.startupLoggedOut()
        }

        handler.postDelayed(runnable, 1000)
    }

}
