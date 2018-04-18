package co.ello.android.ello

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


interface Queue {
    fun <T> add(request: Request<T>)
}


class VolleyQueue(val queue: RequestQueue) : Queue {
    constructor(context: Context) : this(Volley.newRequestQueue(context))

    override fun <T> add(request: Request<T>) {
        queue.add(request)
    }
}


class MockQueue() : Queue {
    override fun <T> add(request: Request<T>) {
    }
}
