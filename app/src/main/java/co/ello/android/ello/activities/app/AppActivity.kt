package co.ello.android.ello

import android.content.Intent
import android.os.Bundle


class AppActivity : ElloActivity() {
    private var screen: AppProtocols.Screen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screen = AppScreen(this)
        setContentView(screen.contentView)
        this.screen = screen
    }

    override fun onStart() {
        super.onStart()
        showLoginScreen()
    }

    fun showLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
