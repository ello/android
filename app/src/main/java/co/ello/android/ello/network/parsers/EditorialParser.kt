package co.ello.android.ello

import java.net.URL


class EditorialParser : IdParser(table = MappingType.Editorials) {
    init {
        linkObject(MappingType.PostsType)
    }

    override fun parse(
            json: JSON
    ): Editorial {
        val kind = Editorial.Kind.create(json["kind"].stringValue)

        // val query = json["stream"]["query"].string?.toURL()
        // val tokens = json["stream"]["tokens"].list?.map { it.stringValue }
        val url = json["url"].string?.toURL()
        val path = json["path"].string?.let { "${Globals.baseURL}$it" }?.toURL()

        val editorial = Editorial(
            id = json["id"].idValue,
            kind = kind,
            title = json["title"].stringValue,
            subtitle = json["subtitle"].string,
            renderedSubtitle = json["renderedSubtitle"].string,
            url = url,
            path = path
        )

        for (size in Editorial.Size.all) {
            if (!json[size.value].exists)  continue
            editorial.images[size] = AssetParser().parse(json[size.value])
        }

        editorial.mergeLinks(json["links"])

        return editorial
    }
}
