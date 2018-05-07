package co.ello.android.ello

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture
import java.util.UUID


class GraphQLRequest<T>(
        val endpointName: String,
        override val requiresAnyToken: Boolean,
        override val supportsAnonymousToken: Boolean
) : Request<JSON>(Request.Method.POST, "${BuildConfig.NINJA_DOMAIN}/api/v3/graphql", null), AuthenticationEndpoint {
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
        data class bool(override val name: String           , override val value: Boolean)  : Variable() { override val type: String get() { return "Bool!"  }}
        data class optionalBool(override val name: String   , override val value: Boolean?) : Variable() { override val type: String get() { return "Bool"   }}
        data class enum(override val name: String           , override val value: String, val typeName: String) : Variable() { override val type: String get() { return "$typeName!" }}
        data class optionalEnum(override val name: String   , override val value: String?, val typeName: String) : Variable() { override val type: String get() { return typeName }}
    }

    private val headers = HashMap<String, String>()
    private var successCompletion: ((JSON) -> Unit)? = null
    private var failureCompletion: ((Throwable) -> Unit)? = null

    private var parserCompletion: ((JSON) -> T)? = null
    private var variables: List<Variable>? = null
    private var fragments: List<Fragments>? = null
    private var body: Fragments? = null
    private var uuid: UUID? = null

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

    fun enqueue(queue: Queue, prevFuture: CompletableFuture<T>? = null): CompletableFuture<T> {
        val future = prevFuture ?: CompletableFuture<T>()

        this.onSuccess { json ->
            val resultJson = json["data"][endpointName]
            try {
                val result = parserCompletion!!.invoke(resultJson)
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
                AuthToken.shared.tokenWithBearer?.let {
                    this.addHeader("Authorization", it)
                }

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
            return "$${variable.name}: ${variable.type}"
        }.joinToString(", ") }
    }

    private fun endpointVariables(): String? {
        return variables?.let { it.map { variable ->
            return "${variable.name}: $${variable.name}"
        }.joinToString(", ") }
    }

    override fun getBody(): ByteArray {
        var query = ""
        val variables = this.variables
        val fragments = body?.dependencies

        if (fragments != null && fragments.isNotEmpty()) {
            val fragmentsQuery = Fragments.flatten(fragments)
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
