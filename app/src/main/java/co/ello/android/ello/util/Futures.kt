package co.ello.android.ello

import java.util.concurrent.CompletableFuture

fun <A> CompletableFuture<A>.onFailure(f: (Throwable) -> Unit): CompletableFuture<A> =
        exceptionally { throwable: Throwable? ->
            throwable?.let { f(it.cause ?: it) }
            null
        }

fun <A> CompletableFuture<A>.onSuccess(f: (A) -> Unit): CompletableFuture<A> {
    thenAccept(f)
    return this
}
