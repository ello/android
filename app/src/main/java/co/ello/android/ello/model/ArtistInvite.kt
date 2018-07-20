package co.ello.android.ello

import java.util.Date
import java.net.URL


data class ArtistInvite(
    val id: String,
    val slug: String,
    val title: String,
    val shortDescription: String,
    val submissionBody: String,
    val longDescription: String,
    val inviteType: String,
    val status: Status,
    val openedAt: Date?,
    val closedAt: Date?
    ) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.ArtistInvitesType)
    override fun update(property: Property, value: Any) {}

    var headerImage: Asset? = null
    var logoImage: Asset? = null
    var guide: List<Guide> = emptyList()
    var approvedSubmissionsStream: Stream? = null
    var selectedSubmissionsStream: Stream? = null
    var unapprovedSubmissionsStream: Stream? = null
    var declinedSubmissionsStream: Stream? = null

    val shareLink: String get() = "${Globals.baseURL}/artist-invite/${this.slug}"
    val hasAdminLinks: Boolean get() = approvedSubmissionsStream != null && unapprovedSubmissionsStream != null

    data class Guide(val title: String, val html: String)

    data class Stream(
        val url: URL,
        val label: String,
        val submissionsStatus: ArtistInviteSubmission.Status
        ) {

        companion object {
            fun create(json: JSON, submissionsStatus: ArtistInviteSubmission.Status): Stream? {
                val url = json["href"].url ?: return null
                val label = json["label"].string ?: return null

                return Stream(
                    url = url,
                    label = label,
                    submissionsStatus = submissionsStatus
                    )
            }
        }
    }

    enum class Status(val value: String) {
        Preview("preview"),
        Upcoming("upcoming"),
        Open("open"),
        Selecting("selecting"),
        Closed("closed");

        companion object {
            fun create(value: String): Status? = when(value) {
                "preview" -> Status.Preview
                "upcoming" -> Status.Upcoming
                "open" -> Status.Open
                "selecting" -> Status.Selecting
                "closed" -> Status.Closed
                else -> null
            }
        }
    }

}
