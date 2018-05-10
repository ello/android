package co.ello.android.ello

import android.content.res.Resources
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.UUID


val String.uppercaseFirst: String get() = when (this.length) {
    0, 1 -> this.toUpperCase()
    else -> this[0].toUpperCase() + this.substring(1)
}

val String.snakeCase: String get() {
    var text = ""
    var isFirst = true
    this.forEach {
        if (it.isUpperCase()) {
            if (!isFirst) text += "_"
            text += it.toLowerCase()
        } else {
            text += it
        }
        isFirst = false
    }
    return text
}

val String.camelCase: String get() = this.split('_')
    .map { it.uppercaseFirst }
    .joinToString("")

fun String.toDate(): Date? {
    val tz = TimeZone.getTimeZone("UTC")
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    df.timeZone = tz

    try {
        return df.parse(this)
    }
    catch(e: ParseException) {
        return null
    }
}

val String.toRequestMethod: ElloRequest.Method? get() = when(this.toUpperCase()) {
    "GET" -> ElloRequest.Method.GET
    "POST" -> ElloRequest.Method.POST
    "PUT" -> ElloRequest.Method.PUT
    "DELETE" -> ElloRequest.Method.DELETE
    else -> null
}

fun randomUUID(): UUID = UUID.randomUUID()
fun UUIDString(): String = UUID.randomUUID().toString()

fun T(res: Int): String = Resources.getSystem().getString(res)
