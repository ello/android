package co.ello.android.ello

import com.squareup.otto.Bus


interface EventBus {
    fun post(event: Any)
    fun register(listener: Any)
    fun unregister(listener: Any)
}

class OttoBus : EventBus {
    private val bus = Bus()

    override fun post(event: Any) {
        bus.post(event)
    }

    override fun register(listener: Any) {
        bus.register(listener)
    }

    override fun unregister(listener: Any) {
        bus.unregister(listener)
    }
}
