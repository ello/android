package co.ello.android.ello

import java.util.Date


data class Relationship(
    val id: String,
    val ownerId: String,
    val subjectId: String
    ) : Model() {

    val owner: User? get() = getLinkObject("owner")
    val subject: User? get() = getLinkObject("subject")
}
