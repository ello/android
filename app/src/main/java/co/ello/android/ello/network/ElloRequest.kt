package co.ello.android.ello

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture
import java.util.UUID


class ElloRequest<T>(
        method: Int,
        path: String,
        override val requiresAnyToken: Boolean,
        override val supportsAnonymousToken: Boolean
) : Request<String>(method, "${BuildConfig.NINJA_DOMAIN}$path", null), AuthenticationEndpoint {
    class CancelledRequest : Throwable()

    constructor(
        method: Method,
        path: String,
        parameters: Map<String, Any>? = null,
        requiresAnyToken: Boolean,
        supportsAnonymousToken: Boolean
    ) : this(when(method) {
            Method.GET -> Request.Method.GET
            Method.POST -> Request.Method.POST
            Method.PUT -> Request.Method.PUT
            Method.DELETE -> Request.Method.DELETE
        }, path, requiresAnyToken, supportsAnonymousToken) {
        if (parameters != null) {
            setBody(parameters)
        }
    }

    private val headers = HashMap<String, String>()
    private var successCompletion: ((String) -> Unit)? = null
    private var parserCompletion: ((Gson, String) -> T)? = null
    private var failureCompletion: ((Throwable) -> Unit)? = null
    private val gson = Gson()
    private var uuid: UUID? = null

    enum class Method {
        GET, POST, PUT, DELETE
    }

    private var body: ByteArray? = null

    fun parser(completion: ((Gson, String) -> T)?): ElloRequest<T> {
        parserCompletion = completion
        return this
    }

    fun addHeader(name: String, value: String): ElloRequest<T> {
        headers[name] = value
        return this
    }

    fun setBody(body: String): ElloRequest<T> {
        this.body = body.toByteArray()
        return this
    }

    fun setBody(body: Map<String, Any>): ElloRequest<T> {
        return setBody(gson.toJson(body))
    }

    fun enqueue(queue: Queue, prevFuture: CompletableFuture<T>? = null): CompletableFuture<T> {
        val future = prevFuture ?: CompletableFuture<T>()

        this.onSuccess { json ->
            try {
                val result = parserCompletion!!.invoke(gson, json)
                future.complete(result)
            }
            catch(e: Throwable) {
                future.completeExceptionally(e)
            }
        }
        .onFailure { exception ->
            future.completeExceptionally(exception)
        }

        AuthenticationManager(queue).attemptRequest(this,
            retry = { this.enqueue(queue, prevFuture = future) },
            proceed = { uuid ->
                this.uuid = uuid
                queue.add(this)
            },
            cancel = {
                future.completeExceptionally(CancelledRequest())
            })

        return future
    }

    override fun deliverError(error: VolleyError) {
        failureCompletion?.invoke(error)
    }

    private fun onSuccess(completion: ((String) -> Unit)?): ElloRequest<T> {
        successCompletion = completion
        return this
    }

    private fun onFailure(completion: ((Throwable) -> Unit)?): ElloRequest<T> {
        failureCompletion = completion
        return this
    }

    override fun getHeaders(): Map<String, String> {
        return headers
    }

    override fun getBody(): ByteArray {
        return body ?: ByteArray(0)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
        val parsed = String(response.data)
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: String) {
        successCompletion?.invoke(response)
    }

}
