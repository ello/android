package co.ello.android.ello


data class TextRegion(
    val content: String
    ) : Model(), Regionable {

    override val identifier = null
    override fun update(property: Property, value: Any) {}

    override var isRepost: Boolean = false
    override val kind: RegionKind get() = RegionKind.Text
}
