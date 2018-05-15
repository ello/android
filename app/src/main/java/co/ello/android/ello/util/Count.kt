package co.ello.android.ello


sealed class Count {
    object Hidden : Count()
    data class Visible(val count: Int) : Count()

    val isVisible: Boolean get() = this is Visible
}
