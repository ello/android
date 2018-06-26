package co.ello.android.ello

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import org.json.JSONException
import java.util.concurrent.CompletableFuture
import java.util.UUID


class ElloRequest<T>(
        method: Int,
        path: String,
        override val requiresAnyToken: Boolean = true,
        override val supportsAnonymousToken: Boolean = true,
        val shouldRetryAuth: Boolean = true
) : Request<String>(method, "${API.domain}$path", null), AuthenticationEndpoint {
    object CancelledRequest : Throwable()

    constructor(
        method: Method,
        path: String,
        parameters: Map<String, Any>? = null,
        requiresAnyToken: Boolean = true,
        supportsAnonymousToken: Boolean = true,
        shouldRetryAuth: Boolean = true
    ) : this(when(method) {
            Method.GET -> Request.Method.GET
            Method.POST -> Request.Method.POST
            Method.PUT -> Request.Method.PUT
            Method.DELETE -> Request.Method.DELETE
        }, path, requiresAnyToken, supportsAnonymousToken, shouldRetryAuth) {
        if (parameters != null) {
            setBody(parameters)
        }
    }

    private val headers = HashMap<String, String>()
    private var successCompletion: ((String) -> Unit)? = null
    private var parserCompletion: ((JSON) -> T)? = null
    private var failureCompletion: ((Throwable) -> Unit)? = null
    private var uuid: UUID? = null
    private var manager: AuthenticationManager? = null
    private var retryBlock: Block? = null
    private var cancelBlock: Block? = null
    private val future = CompletableFuture<T>()

    enum class Method {
        GET, POST, PUT, DELETE
    }

    private var body: ByteArray? = null

    init {
        this.addHeader("Accept", "application/json")
        this.addHeader("Content-Type", "application/json")
    }

    fun parser(completion: ((JSON) -> T)?): ElloRequest<T> {
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
        val gson = Gson()
        return setBody(gson.toJson(body))
    }

    fun enqueue(queue: Queue): CompletableFuture<T> {
        retryBlock = {
            this.enqueue(queue)
        }
        cancelBlock = { future.completeExceptionally(CancelledRequest) }

        this.onSuccess { jsonString ->
            try {
                val json = JSON(jsonString)
                val result = parserCompletion!!.invoke(json)
                future.complete(result)
            }
            catch(e: Throwable) {
                future.completeExceptionally(e)
            }
        }
        .onFailure { exception ->
            future.completeExceptionally(exception)
        }

        val manager = AuthenticationManager(queue)
        this.manager = manager
        manager.attemptRequest(this,
            retry = retryBlock!!,
            proceed = { uuid ->
                AuthToken.shared.tokenWithBearer?.let {
                    this.addHeader("Authorization", it)
                }

                this.uuid = uuid
                queue.add(this)
            },
            cancel = cancelBlock!!)

        return future
    }

    override fun deliverError(error: VolleyError) {
        if (error is AuthFailureError && shouldRetryAuth) {
            manager!!.attemptAuthentication(uuid!!, RequestAttempt(this, retryBlock!!, cancelBlock!!))
        }
        else {
            val data = error.networkResponse.data
            try {
                val json = JSON(data)
                val netError = NetworkError(json)
                failureCompletion?.invoke(netError)
            }
            catch (e: JSONException) {
                failureCompletion?.invoke(error)
            }
        }
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
