package co.ello.android.ello

import android.content.Context
import android.hardware.Sensor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.orhanobut.hawk.Hawk
import co.ello.android.ello.debug.ShakeController
import co.ello.android.ello.debug.DebugDialogFragment
import android.hardware.SensorManager
import co.ello.android.ello.debug.ShakeController.OnShakeListener
import android.os.Vibrator

var App: AppActivity? = null

class AppActivity : AppCompatActivity() {
    private var controller: RootController? = null
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var shakeController: ShakeController = ShakeController()

    companion object {
        val ENVIRONMENT_KEY = "ENV_KEY"
        val DEFAULT_ENVIRONMENT_KEY = "Rainbow"
        val DEBUG_DIALOG_TAG = "DebugDialogFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App = this

        Hawk.init(this).build()
        API.init(Hawk.get(ENVIRONMENT_KEY, DEFAULT_ENVIRONMENT_KEY))
        AuthToken.init()
        
        supportActionBar?.hide()
        showAppScreen()

        if (!Hawk.contains(ENVIRONMENT_KEY)) {
            showDebugDialog()
        }
        initShakeGesture()
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(shakeController, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        sensorManager?.unregisterListener(shakeController)
        super.onPause()
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

    private fun vibrateDevice(milliseconds: Long) {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(milliseconds)
    }

    private fun showDebugDialog() {
        val dialog = DebugDialogFragment()
        dialog.show(supportFragmentManager, DEBUG_DIALOG_TAG)
    }

    private fun initShakeGesture() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.sensorManager = sensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeController.setOnShakeListener(object : OnShakeListener {
            override fun onShake() {
                vibrateDevice(250)
                showDebugDialog()
            }
        })
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
