package co.ello.android.ello

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.orhanobut.hawk.Hawk


class AppActivity : AppCompatActivity() {
    private var controller: RootController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Hawk.init(this).build()
        AuthToken.init()

        supportActionBar?.hide()
        showAppScreen()
    }

    private fun showAppScreen() {
        val controller = AppController(this)
        setContentView(controller.view)
        controller.start()
        controller.appear()
        this.controller = controller
    }

    override fun onDestroy() {
        controller?.disappear()
        controller?.finish()
        super.onDestroy()
    }
}
