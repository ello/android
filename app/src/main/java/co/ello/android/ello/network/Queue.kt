package co.ello.android.ello

import android.content.Context
import com.android.volley.toolbox.StringRequest
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

interface Queue {
    fun add(request: StringRequest)
}

class VolleyQueue(val queue: RequestQueue) : Queue {
    constructor(context: Context) : this(Volley.newRequestQueue(context))

    override fun add(request: StringRequest) {
        queue.add(request)
    }
}

class MockQueue() : Queue {
    var requests = ArrayList<StringRequest>()

    override fun add(request: StringRequest) {
        requests.add(request)
    }
}
