package co.ello.android.ello


sealed class Result<out T>
data class Success<out T>(val value: T) : Result<T>()
data class Failure(val error: Throwable) : Result<Nothing>()

fun Success(): Result<Unit> = Success(Unit)
