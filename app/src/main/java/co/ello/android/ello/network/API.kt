package co.ello.android.ello

import com.google.gson.Gson
import java.util.concurrent.CompletableFuture


class API(val queue: Queue) {
    fun login(username: String, password: String): CompletableFuture<Credentials> {
        val future = CompletableFuture<Credentials>()
        val path = "/oauth/token"

        val elloRequest = ElloRequest(ElloRequest.Method.POST, path)
            .onSuccess { json ->
                val gson = Gson()
                val credentials = gson.fromJson(json, Credentials::class.java)
                future.complete(credentials)
            }
            .onFailure { exception ->
                future.completeExceptionally(exception)
            }

        elloRequest.addHeader("Accept", "application/json")
        elloRequest.addHeader("Content-Type", "application/json")
        elloRequest.body = mapOf(
            "email" to username,
            "password" to password,
            "client_id" to BuildConfig.NINJA_CLIENT_KEY,
            "client_secret" to BuildConfig.NINJA_CLIENT_SECRET,
            "grant_type" to "password"
        )

        queue.add(elloRequest)
        return future
    }

    fun join(email: String, username: String, password: String): CompletableFuture<Credentials> {
        val future = CompletableFuture<Credentials>()
        val path = "/oauth/token"

        val elloRequest = ElloRequest(ElloRequest.Method.POST, path)
            .onSuccess { json ->
                val gson = Gson()
                val credentials = gson.fromJson(json, Credentials::class.java)
                future.complete(credentials)
            }
            .onFailure { exception ->
                future.completeExceptionally(exception)
            }

        elloRequest.addHeader("Accept", "application/json")
        elloRequest.addHeader("Content-Type", "application/json")
        elloRequest.body = mapOf(
            "email" to email,
            "username" to username,
            "password" to password,
            "client_id" to BuildConfig.NINJA_CLIENT_KEY,
            "client_secret" to BuildConfig.NINJA_CLIENT_SECRET,
            "grant_type" to "password"
        )

        queue.add(elloRequest)
        return future
    }
}
