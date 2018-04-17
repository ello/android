package co.ello.android.ello

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture


class API(val queue: Queue) {
    fun login(username: String, password: String): CompletableFuture<Credentials> {
        val future = CompletableFuture<Credentials>()
        val url = "https://ello.co/api/oauth/token"

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { json ->
                val gson = Gson()
                val credentials = gson.fromJson(json, Credentials::class.java)
                future.complete(credentials)
            },
            Response.ErrorListener { exception ->
                future.completeExceptionally(exception)
            }) {

            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }

            override fun getBody(): ByteArray {
                val gson = Gson()
                val vals = mapOf(
                    "email" to username,
                    "password" to password,
                    "client_id" to BuildConfig.PROD_CLIENT_KEY,
                    "client_secret" to BuildConfig.PROD_CLIENT_SECRET,
                    "grant_type" to "password"
                )
                val json = gson.toJson(vals)
                return json.toByteArray()
            }
        }

        queue.add(stringRequest)
        return future
    }
}
