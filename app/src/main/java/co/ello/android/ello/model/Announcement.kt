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

    val preferredAttachment: Attachment? get() = image?.hdpi
    val imageURL: URL? get() = preferredAttachment?.url

}
