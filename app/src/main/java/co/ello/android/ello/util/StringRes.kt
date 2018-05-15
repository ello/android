package co.ello.android.ello


sealed class StringRes {
    abstract fun gen(): String
}

data class Lit(val value: String) : StringRes() {
    override fun gen(): String = this.value
}

data class Res(val value: Int) : StringRes() {
    override fun gen(): String = T(value)
}
