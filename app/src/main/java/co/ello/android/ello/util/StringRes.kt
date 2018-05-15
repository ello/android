package co.ello.android.ello

import android.content.Context


sealed class StringRes {
    open fun gen(context: Context): String = ""
}

data class Lit(val value: String) : StringRes() {
    override fun gen(context: Context): String = this.value
}

data class Res(val value: Int) : StringRes() {
    override fun gen(context: Context): String = context.resources.getString(value)
}
