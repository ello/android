package co.ello.android.ello


data class ArtistInviteSubmission(
    val id: String,
    val artistInviteId: String,
    val postId: String,
    val status: Status
    ) : Model() {

    enum class Status(val value: String) {
        Approved("approved"),
        Selected("selected"),
        Unapproved("unapproved"),
        Unspecified("unspecified"),
        Declined("declined");

        companion object {
            fun create(value: String): Status? = when(value) {
                "approved" -> Status.Approved
                "selected" -> Status.Selected
                "unapproved" -> Status.Unapproved
                "unspecified" -> Status.Unspecified
                "declined" -> Status.Declined
                else -> null
            }
        }
    }

    data class Action(
        val name: Name,
        val label: String,
        val request: ElloRequest<CategoryPost>
        ) {

        enum class Name(val value: String) {
            Approve("approve"),
            Unapprove("unapprove"),
            Select("select"),
            Unselect("unselect"),
            Decline("decline");

            companion object {
                fun create(value: String): Name? = when(value) {
                    "approve" -> Name.Approve
                    "unapprove" -> Name.Unapprove
                    "select" -> Name.Select
                    "unselect" -> Name.Unselect
                    "decline" -> Name.Decline
                    else -> null
                }
            }
        }

        companion object {
            fun create(nameStr: String, json: JSON): Action? {
                val method = json["method"].string?.toRequestMethod ?: return null
                val path = json["href"].string ?: return null
                val label = json["label"].string ?: return null
                val parameters = json["body"].obj ?: return null
                val name = Name.create(nameStr) ?: return null
                return Action(name = name, label = label, request = ElloRequest<CategoryPost>(method = method, path = path, parameters = parameters))
            }
        }
    }

    val artistInvite: ArtistInvite? get() = getLinkObject("artistInvite")
    val post: Post? get() = getLinkObject("post")
    val user: User? get() = getLinkObject("user")

}
