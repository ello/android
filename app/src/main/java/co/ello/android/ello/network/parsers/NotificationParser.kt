package co.ello.android.ello.network.parsers

import co.ello.android.ello.*
import java.util.*

/**
 * Created by sahitpenmatcha on 7/11/18.
 */
class NotificationParser : IdParser(table = MappingType.Notifications) {
    init {
        linkObject(MappingType.PostsType)
    }

    override fun parse(
            json: JSON
    ): Notification {
       // val kind = Notification.Kind.create(json["kind"].stringValue)

        // val query = json["stream"]["query"].string?.toURL()
        // val tokens = json["stream"]["tokens"].list?.map { it.stringValue }
        val url = json["url"].string?.toURL()
        val path = json["path"].string?.let { "${Globals.baseURL}$it" }?.toURL()

        val createdAt = Date(json["createdAt"].stringValue)
        val kind = Activity.Kind.create(json["kind"].stringValue)
        val id = json["id"].idValue
        val subjectType = Activity.SubjectType.create(json["subjectType"].stringValue)

        val activity = Activity(createdAt = createdAt, kind = kind, id=id, subjectType = subjectType)

        val notification = Notification(
                activity
                //need to get the activity here from json object
                //need to figure out these parameters
//                id = json["id"].idValue,
//                kind = kind,
//                title = json["title"].stringValue,
//                subtitle = json["subtitle"].string,
//                renderedSubtitle = json["renderedSubtitle"].string,
//                url = url,
//                path = path
        )

        for (size in Editorial.Size.all) {
            if (!json[size.value].exists)  continue
           // editorial.images[size] = AssetParser().parse(json[size.value])
        }

        notification.mergeLinks(json["links"])

        return notification
    }
}