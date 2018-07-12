package co.ello.android.ello.debug

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Created by sahitpenmatcha on 7/12/18.
 */
class ShakeController: SensorEventListener {
    /*
	 * The gForce that is necessary to register as shake.
	 * Must be greater than 1G (one earth gravity unit).
	 */
    private val SHAKE_THRESHOLD_GRAVITY = 2f
    private val SHAKE_SLOP_TIME_MS = 500
    private val SHAKE_COUNT_RESET_TIME_MS = 3000

    private var listener: OnShakeListener? = null
    private var shakeTimestamp: Long = 0
    private var shakeCount: Int = 0

    fun setOnShakeListener(listener: OnShakeListener) {
        this.listener = listener
    }

    interface OnShakeListener {
        fun onShake()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {

        if (listener != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            // gForce close to 1 when there is no movement.
            val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ) as Double)

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                val now = System.currentTimeMillis()
                // ignore shake events too close to each other (500ms)
                if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return
                }

                // reset the shake count after 3 seconds of no shakes
                if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    shakeCount = 0
                }

                shakeTimestamp = now
                shakeCount++

                listener!!.onShake()
            }
        }
    }
}