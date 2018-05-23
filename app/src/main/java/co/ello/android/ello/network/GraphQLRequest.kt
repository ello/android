package co.ello.android.ello

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture
import java.util.UUID


class GraphQLRequest<T>(
        val endpointName: String,
        override val requiresAnyToken: Boolean = true,
        override val supportsAnonymousToken: Boolean = true
) : Request<JSON>(Request.Method.POST, "${API.domain}/api/v3/graphql", null), AuthenticationEndpoint {
    class CancelledRequest : Throwable()
    class ParsingError : Throwable()

    sealed class Variable {
        abstract val name: String
        abstract val type: String
        abstract val value: Any?

        data class string(override val name: String         , override val value: String)   : Variable() { override val type: String get() { return "String!"}}
        data class optionalString(override val name: String , override val value: String?)  : Variable() { override val type: String get() { return "String" }}
        data class int(override val name: String            , override val value: Int)      : Variable() { override val type: String get() { return "Int!"   }}
        data class optionalInt(override val name: String    , override val value: Int?)     : Variable() { override val type: String get() { return "Int"    }}
        data class float(override val name: String          , override val value: Float)    : Variable() { override val type: String get() { return "Float!" }}
        data class optionalFloat(override val name: String  , override val value: Float?)   : Variable() { override val type: String get() { return "Float"  }}
        data class boolean(override val name: String           , override val value: Boolean)  : Variable() { override val type: String get() { return "Bool!"  }}
        data class optionalBoolean(override val name: String   , override val value: Boolean?) : Variable() { override val type: String get() { return "Bool"   }}
        data class enum(override val name: String           , override val value: String, val typeName: String) : Variable() { override val type: String get() { return "$typeName!" }}
        data class optionalEnum(override val name: String   , override val value: String?, val typeName: String) : Variable() { override val type: String get() { return typeName }}
    }

    private val headers = HashMap<String, String>()
    private var successCompletion: ((JSON) -> Unit)? = null
    private var failureCompletion: ((Throwable) -> Unit)? = null

    private var parserCompletion: ((JSON) -> T)? = null
    private var variables: List<Variable>? = null
    private var body: Fragments? = null
    private var uuid: UUID? = null
    private var manager: AuthenticationManager? = null
    private var retryBlock: Block? = null
    private var cancelBlock: Block? = null
    private val future = CompletableFuture<T>()

    init {
        this.addHeader("Accept", "application/json")
        this.addHeader("Content-Type", "application/json")
    }

    fun parser(parser: ((JSON) -> T)): GraphQLRequest<T> {
        parserCompletion = parser
        return this
    }

    fun setVariables(vararg variables: Variable): GraphQLRequest<T> {
        this.variables = List<Variable>(variables.size, { variables[it] })
        return this
    }

    fun setBody(body: Fragments): GraphQLRequest<T> {
        this.body = body
        return this
    }

    fun enqueue(queue: Queue): CompletableFuture<T> {
        setRetryPolicy(DefaultRetryPolicy(
                30_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))

        retryBlock = { this.enqueue(queue) }
        cancelBlock = { future.completeExceptionally(CancelledRequest()) }

        this.onSuccess { json ->
            val resultJson = json["data"][endpointName]
            var result: T? = null
            try {
                result = parserCompletion!!.invoke(resultJson)
            }
            catch(e: Throwable) {
                future.completeExceptionally(e)
            }

            if (result != null) {
                future.complete(result)
            }
        }
        .onFailure { exception ->
            println("${this.endpointName} failed: $exception")
            if (exception is VolleyError && exception.networkResponse != null) {
                println("server error:\n${String(exception.networkResponse.data)}")
            }

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
        if (error is AuthFailureError) {
            manager!!.attemptAuthentication(uuid!!, RequestAttempt(this, retryBlock!!, cancelBlock!!))
        }
        else {
            failureCompletion?.invoke(error)
        }
    }

    private fun onSuccess(completion: ((JSON) -> Unit)?): GraphQLRequest<T> {
        successCompletion = completion
        return this
    }

    private fun onFailure(completion: ((Throwable) -> Unit)?): GraphQLRequest<T> {
        failureCompletion = completion
        return this
    }

    fun addHeader(name: String, value: String): GraphQLRequest<T> {
        headers[name] = value
        return this
    }

    override fun getHeaders(): Map<String, String> {
        return headers
    }

    private fun queryVariables(): String? {
        return variables?.let { it.map { variable ->
            "$${variable.name}: ${variable.type}"
        }.joinToString(", ") }
    }

    private fun endpointVariables(): String? {
        return variables?.let { it.map { variable ->
            "${variable.name}: $${variable.name}"
        }.joinToString(", ") }
    }

    override fun getBody(): ByteArray {
        var query = ""
        val variables = this.variables
        val fragments = body?.dependencies

        if (fragments != null && fragments.isNotEmpty()) {
            val fragmentsQuery = fragments.distinctBy { it.string }.map { it.string }.joinToString("\n")
            query += fragmentsQuery + "\n"
        }

        val queryVariables = this.queryVariables()
        if (queryVariables != null && queryVariables.isNotEmpty()) {
            query += "query($queryVariables)\n"
        }

        query += "{\n$endpointName"
        val endpointVariables = this.endpointVariables()
        if (endpointVariables != null && endpointVariables.isNotEmpty()) {
            query += "($endpointVariables)"
        }

        val queryBody = body?.string ?: ""
        query += "\n  {\n$queryBody\n  }\n}"

        val httpBody: MutableMap<String, Any> = mutableMapOf("query" to query)

        if (variables != null && variables.isNotEmpty()) {
            val variablesMap: MutableMap<String, Any?> = mutableMapOf()
            for (variable in variables) {
                variablesMap[variable.name] = variable.value
            }
            httpBody["variables"] = variablesMap
        }

        val gson = Gson()
        val json = gson.toJson(httpBody)
        return json.toByteArray()
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSON> {
        val json = JSON(response.data)
        return Response.success(json, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: JSON) {
        successCompletion?.invoke(response)
    }

}
