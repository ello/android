package co.ello.android.ello

import java.net.URL


data class PageHeader(
    val id: String,
    val postToken: String?,
    val categoryId: String?,
    val header: String,
    val subheader: String,
    val ctaCaption: String,
    val ctaURL: URL?,
    val isSponsored: Boolean,
    val image: Asset?,
    val kind: Kind
    ) : Model() {

    val user: User? get() = getLinkObject("user")

    enum class Kind(val value: String) {
        ArtistInvite("ARTIST_INVITE"),
        Category("CATEGORY"),
        Editorial("EDITORIAL"),
        Generic("GENERIC");

        companion object {
            fun create(value: String): Kind? = when(value) {
                "ARTIST_INVITE" -> Kind.ArtistInvite
                "CATEGORY" -> Kind.Category
                "EDITORIAL" -> Kind.Editorial
                "GENERIC" -> Kind.Generic
                else -> null
            }
        }
    }

}
