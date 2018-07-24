package co.ello.android.ello


class AttachmentParser : IdParser(table = MappingType.AttachmentsType) {
    override fun parse(json: JSON): Attachment {
        val url = json["url"].url!!
        val attachment = Attachment(url)
        attachment.type = json["metadata"]["type"].string
        attachment.size = json["metadata"]["size"].int
        attachment.width = json["metadata"]["width"].int
        attachment.height = json["metadata"]["height"].int
        return attachment
    }
}
