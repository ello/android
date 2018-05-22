package co.ello.android.ello


class StreamCellItem(
    val model: Model? = null,
    val type: StreamCellType,
    var placeholderType: StreamCellType.PlaceholderType? = null
    )
{
    val uuid: String = UUIDString()
    var height: Int? = null
    var extra: Any? = null
}
