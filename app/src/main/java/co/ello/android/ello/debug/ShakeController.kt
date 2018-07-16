package co.ello.android.ello.debug

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ShakeController: SensorEventListener {
    /*
	 * The gForce that is necessary to register as shake.
	 * Must be greater than 1G (one earth gravity unit).
	 */
    private val SHAKE_THRESHOLD_GRAVITY = 2f
    private val SHAKE_SLOP_TIME_MS = 500

    private var listener: OnShakeListener? = null
    private var shakeTimestamp: Long = 0

    fun setOnShakeListener(listener: OnShakeListener) {
        this.listener = listener
    }

    interface OnShakeListener {
        fun onShake()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    private fun isShakeEvent(event: SensorEvent): Boolean {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val gX = x / SensorManager.GRAVITY_EARTH
        val gY = y / SensorManager.GRAVITY_EARTH
        val gZ = z / SensorManager.GRAVITY_EARTH

        // gForce close to 1 when there is no movement.
        return Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()) > SHAKE_THRESHOLD_GRAVITY
    }

    private fun isRecentShake(now: Long) : Boolean {
        // ignore shake events too close to each other (500ms)
        if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
            return true
        }
        shakeTimestamp = now
        return false
    }

    override fun onSensorChanged(event: SensorEvent) {
        val listener = this.listener ?: return
        if (!isShakeEvent(event))  return
        if (isRecentShake(System.currentTimeMillis()))  return

        listener.onShake()
    }
}