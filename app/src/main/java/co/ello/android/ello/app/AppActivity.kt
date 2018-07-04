package co.ello.android.ello

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.orhanobut.hawk.Hawk


var App: AppActivity? = null

class AppActivity : AppCompatActivity() {
    private var controller: RootController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App = this

        Hawk.init(this).build()
        API.init()
        AuthToken.init()

        supportActionBar?.hide()
        showAppScreen()
    }

    private fun showAppScreen() {
        var controller = getLastCustomNonConfigurationInstance() as? AppController
        if (controller == null) {
            controller = AppController(this)
            controller.start()
            setContentView(controller.view)
            controller.appear()
        }
        else {
            controller.rotate()
            setContentView(controller.view)
        }

        this.controller = controller
    }

    override fun onDestroy() {
        controller?.let {
            it.disappear()
            it.finish()
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (controller?.history?.goBack() == BackStack.StatusCode.Success)  return
        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        val controller = this.controller
        if (controller != null) {
            this.controller = null
            setContentView(View(this))
            return controller
        }
        else  return super.onRetainCustomNonConfigurationInstance()
    }
}
