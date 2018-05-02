package co.ello.android.ello


class AssetParser : IdParser(table = MappingType.AssetsType) {
    override fun parse(json: JSON): Asset {
        val asset = Asset(json["id"].stringValue)
        val attachmentsJson = json["attachment"]

        val attachments: List<Pair<JSON, Asset.AttachmentType>> = listOf(
            Pair(attachmentsJson["optimized"], Asset.AttachmentType.Optimized),
            Pair(attachmentsJson["mdpi"], Asset.AttachmentType.Mdpi),
            Pair(attachmentsJson["hdpi"], Asset.AttachmentType.Hdpi),
            Pair(attachmentsJson["xhdpi"], Asset.AttachmentType.Xhdpi),
            Pair(attachmentsJson["original"], Asset.AttachmentType.Original),
            Pair(attachmentsJson["large"], Asset.AttachmentType.Large),
            Pair(attachmentsJson["regular"], Asset.AttachmentType.Regular)
        )
        for ((attachmentJSON, type) in attachments) {
            if (attachmentJSON["url"].string == null) continue
            asset.replace(type = type, withAttachment = Attachment.fromJSON(attachmentJSON))
        }

        return asset
    }
}
