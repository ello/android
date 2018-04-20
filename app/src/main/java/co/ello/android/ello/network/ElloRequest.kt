package co.ello.android.ello

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture


class ElloRequest<T>(
        method: Int,
        url: String
) : Request<String>(method, url, null) {

    constructor(
        method: Method,
        path: String
    ) : this(when(method) {
            Method.GET -> Request.Method.GET
            Method.POST -> Request.Method.POST
            Method.PUT -> Request.Method.PUT
            Method.DELETE -> Request.Method.DELETE
        }, "${BuildConfig.NINJA_DOMAIN}/api$path")

    private val headers = HashMap<String, String>()
    private var successCompletion: ((String) -> Unit)? = null
    private var parserCompletion: ((Gson, String) -> T)? = null
    private var failureCompletion: ((Throwable) -> Unit)? = null
    private val gson = Gson()

    enum class Method {
        GET, POST, PUT, DELETE
    }

    var body: Map<String, Any>? = null

    fun addHeader(name: String, value: String) {
        headers[name] = value
    }

    override fun deliverError(error: VolleyError) {
        failureCompletion?.invoke(error)
    }

    fun enqueue(queue: Queue): CompletableFuture<T> {
        val future = CompletableFuture<T>()

        this.onSuccess { json ->
            val result = parserCompletion?.invoke(gson, json)
            println("result $result")
            if (result != null) {
                future.complete(result)
            }
            // else {
            //     future.completeExceptionally(exception)
            // }
        }
        .onFailure { exception ->
            println("error $exception")
            future.completeExceptionally(exception)
        }

        queue.add(this)
        return future
    }

    fun parser(completion: ((Gson, String) -> T)?): ElloRequest<T> {
        parserCompletion = completion
        return this
    }

    fun onSuccess(completion: ((String) -> Unit)?): ElloRequest<T> {
        successCompletion = completion
        return this
    }

    fun onFailure(completion: ((Throwable) -> Unit)?): ElloRequest<T> {
        failureCompletion = completion
        return this
    }

    override fun getHeaders(): Map<String, String> {
        return headers
    }

    override fun getBody(): ByteArray {
        if (body == null)  return ByteArray(0)

        val json = gson.toJson(body)
        return json.toByteArray()
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
        val parsed = String(response.data)
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: String) {
        successCompletion?.invoke(response)
    }

}
