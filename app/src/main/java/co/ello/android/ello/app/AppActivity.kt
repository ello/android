package co.ello.android.ello

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class AppActivity : AppCompatActivity() {
    private var controller: AppController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        showAppScreen()
    }

    private fun showAppScreen() {
        val controller = AppController(this)
        setContentView(controller.view)
        controller.onAppear()
        this.controller = controller
    }
}
