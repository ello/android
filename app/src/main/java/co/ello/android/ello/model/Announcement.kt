package co.ello.android.ello

import java.net.URL


data class Announcement(
    val id: String,
    val isStaffPreview: Boolean,
    val header: String,
    val body: String,
    val ctaURL: URL?,
    val ctaCaption: String,
    val image: Asset?
    ) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.AnnouncementsType)
    override fun update(property: Property, value: Any) {}

    val preferredAttachment: Attachment? get() = image?.hdpi
    val imageURL: URL? get() = preferredAttachment?.url

}
