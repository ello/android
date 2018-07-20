package co.ello.android.ello.network.parsers

import co.ello.android.ello.*
import java.util.*

class NotificationParser : IdParser(table = MappingType.NotificationsType) {
    init {

    }

    override fun flatten(json: JSON, identifier: Identifier, db: Database) {
        when(Notification.SubjectType.create(json["subjectType"].stringValue)) {
            Notification.SubjectType.Post -> flattenPostSubject(json["subject"], db)
            Notification.SubjectType.User -> flattenUserSubject(json["subject"], db)
            Notification.SubjectType.Comment -> flattenCommentSubject(json["subject"], db)
            else -> null
        }?.let { identifier ->
            json["links"]["subject"] = JSON(mapOf<String, String>(
                    "id" to identifier.id,
                    "type" to identifier.table.name
            ))
        }

        super.flatten(json, identifier, db)
    }

    private fun flattenCommentSubject(json: JSON, db: Database): Identifier? {
        val commentParser = CommentParser()
        return commentParser.identifier(json)?.let { commentIdentifier ->
            commentParser.flatten(json, identifier = commentIdentifier, db = db)
            commentIdentifier
        }
    }

    private fun flattenPostSubject(json: JSON, db: Database): Identifier? {
        val postSubject = PostParser()
        return postSubject.identifier(json)?.let { postIdentifier ->
            postSubject.flatten(json, identifier = postIdentifier, db = db)
            postIdentifier
        }
    }

    private fun flattenUserSubject(json: JSON, db: Database): Parser.Identifier? {
        val userParser = UserParser()
        return userParser.identifier(json)?.let { userIdentifier ->
            userParser.flatten(json, identifier = userIdentifier, db = db)
            userIdentifier
        }
    }

    override fun parse(
            json: JSON
    ): Notification {

        val createdAt = Date(json["createdAt"].stringValue)
        val id = json["id"].idValue
        val kind = Notification.Kind.create(json["kind"].stringValue)
        val subjectType = Notification.SubjectType.create(json["subjectType"].stringValue)

        val notification = Notification(
            id = id,
            createdAt = createdAt,
            kind = kind,
            subjectType = subjectType
        )

        notification.mergeLinks(json["links"])

        return notification
    }
}