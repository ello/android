package co.ello.android.ello


data class TextRegion(
    val content: String
    ) : Model(), Regionable {

    override var isRepost: Boolean = false
    override val kind: RegionKind get() = RegionKind.Text
}
