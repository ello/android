package co.ello.android.ello


class StreamCellItem(val model: Model? = null, val type: StreamCellType) {
    val uuid: String = UUIDString()
    var height: Int? = null
}
