package co.ello.android.ello

import java.util.Date


data class Relationship(
    val id: String,
    val ownerId: String,
    val subjectId: String
    ) : Model() {

    override val identifier = Parser.Identifier(id = id, table = MappingType.RelationshipsType)
    override fun update(property: Property, value: Any) {}

    val owner: User? get() = getLinkObject("owner")
    val subject: User? get() = getLinkObject("subject")
}
