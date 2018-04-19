package co.ello.android.ello

import android.content.Intent
import android.os.Bundle
import android.os.Handler


class AppActivity : ElloActivity() {
    private var screen: AppProtocols.Screen? = null
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screen = AppScreen(this)
        setContentView(screen.contentView)
        this.screen = screen
    }

    override fun onStart() {
        super.onStart()

        val runnable = Runnable {
            showLoginScreen()
        }

        handler.postDelayed(runnable, 1000)
    }

    fun showLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}
