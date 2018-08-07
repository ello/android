package co.ello.android.ello

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NotificationParser : IdParser(table = MappingType.NotificationsType) {

    override fun flatten(json: JSON, identifier: Identifier, db: Database){
        when(Notification.SubjectType.create(json["subjectType"].stringValue)) {
            Notification.SubjectType.Post ->  flattenPostSubject(json["subject"], db)
            Notification.SubjectType.User -> flattenUserSubject(json["subject"], db)
            Notification.SubjectType.Comment -> flattenCommentSubject(json["subject"], db)
            //I think these (below) are wrong
            Notification.SubjectType.Watch -> flattenPostSubject(json["subject"]["post"], db)
            Notification.SubjectType.CategoryPost -> flattenCategoryPostSubject(json["subject"], db)
            Notification.SubjectType.Love -> flattenPostSubject(json["subject"]["post"], db)
            else -> null
        }?.let { identifier ->
            json["links"]["subject"] = JSON(mapOf<String, Any>(
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
        val postParser = PostParser()
        return postParser.identifier(json)?.let { postIdentifier ->
            postParser.flatten(json, identifier = postIdentifier, db = db)
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

    private fun flattenCategoryPostSubject(json: JSON, db: Database): Parser.Identifier? {
        val categoryPostParser = CategoryPostParser()
        return categoryPostParser.identifier(json)?.let { categoryPostIdentifier ->
            categoryPostParser.flatten(json, identifier = categoryPostIdentifier, db = db)
            categoryPostIdentifier
        }
    }

    override fun parse(
            json: JSON
    ): Notification {

        println(json["subjectType"].stringValue)
        println(json["subject"])

        val id = json["id"].idValue
        println(id)
        val kind = Notification.Kind.create(json["kind"].stringValue)
        val subjectType = Notification.SubjectType.create(json["subjectType"].stringValue)
        val createdAt = json["createdAt"].stringValue
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000000Z'")
        var createdAtDate : Date? = null

        try {
            createdAtDate = format.parse(createdAt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val notification = Notification(
            id = id,
            createdAt = createdAtDate as Date,
            kind = kind,
            subjectType = subjectType
        )

        notification.mergeLinks(json["links"])

        return notification
    }
}