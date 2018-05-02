package co.ello.android.ello


class PageHeaderParser : IdParser(table = MappingType.PageHeadersType) {
    init {
        linkObject(MappingType.UsersType)
    }

    override fun parse(json: JSON): PageHeader {
        val kind = json["kind"].string?.let { PageHeader.Kind.create(it) } ?: PageHeader.Kind.Generic
        val image = AssetParser().parse(json["image"])

        val header = PageHeader(
            id = json["id"].stringValue,
            postToken = json["postToken"].string,
            categoryId = json["category"]["id"].id,
            header = json["header"].stringValue,
            subheader = json["subheader"].stringValue,
            ctaCaption = json["ctaLink"]["text"].stringValue,
            ctaURL = json["ctaLink"]["url"].url,
            isSponsored = json["isSponsored"].booleanValue,
            image = image,
            kind = kind
        )

        header.mergeLinks(json["links"])

        return header
    }
}
