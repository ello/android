package co.ello.android.ello

import android.media.Image
import java.net.URL


data class Asset(val id: String) : Model() {

    enum class AttachmentType {
        Optimized,
        Mdpi,
        Hdpi,
        Xhdpi,
        Original,
        Large,
        Regular
    }

    var optimized: Attachment? = null
    var mdpi: Attachment? = null
    var hdpi: Attachment? = null
    var xhdpi: Attachment? = null
    var original: Attachment? = null
    var large: Attachment? = null
    var regular: Attachment? = null

    val allAttachments: List<Pair<AttachmentType, Attachment>> get() {
        val possibles: List<Pair<AttachmentType, Attachment?>> = listOf(
            Pair(AttachmentType.Optimized, optimized),
            Pair(AttachmentType.Mdpi, mdpi),
            Pair(AttachmentType.Hdpi, hdpi),
            Pair(AttachmentType.Xhdpi, xhdpi),
            Pair(AttachmentType.Original, original),
            Pair(AttachmentType.Large, large),
            Pair(AttachmentType.Regular, regular)
        )
        return possibles.flatMap { (type, attachment) ->
            attachment?.let { listOf(Pair(type, attachment)) } ?: emptyList()
        }
    }

    val isGif: Boolean get() = original?.isGif == true || optimized?.isGif == true

    val isLargeGif: Boolean get() {
        val size = optimized?.size
        if (isGif && size != null) {
            return size >= 3145728
        }
        return false
    }

    val isSmallGif: Boolean get() {
        val size = optimized?.size
        if (isGif && size != null) {
            return size <= 1000000
        }
        return false
    }

    val oneColumnAttachment: Attachment? get() {
        // return Window.isWide(Window.width) && DeviceScreen.isRetina ? xhdpi : hdpi
        TODO("Window.isWidth and DeviceScreen.isRetina")
    }

    val gridLayoutAttachment: Attachment? get() {
        // return Window.isWide(Window.width) && DeviceScreen.isRetina ? hdpi : mdpi
        TODO("Window.isWidth and DeviceScreen.isRetina")
    }

    val largeOrBest: Attachment? get() {
        if (isGif && original != null) return original

        var attachment: Attachment? = null
        if (Globals.isRetina) {
            attachment = large ?: xhdpi ?: optimized
        }
        attachment = attachment ?: hdpi ?: regular
        return attachment
    }

    val aspectRatio: Double get() {
        var aspectRatio: Double? = null
        val attachment: Attachment? = oneColumnAttachment ?: optimized
        safeLet(attachment?.width, attachment?.height) { width, height ->
            aspectRatio = width.toDouble() / height.toDouble()
        }
        return aspectRatio ?: 1.3333333333333333
    }

    constructor(url: URL) : this(id = UUIDString()) {
        val attachment = Attachment(url = url)
        this.optimized = attachment
        this.mdpi = attachment
        this.hdpi = attachment
        this.xhdpi = attachment
        this.original = attachment
        this.large = attachment
        this.regular = attachment
    }

    constructor(url: URL, gifData: ByteArray, posterImage: Image) : this(id = UUIDString()) {
        val optimized = Attachment(url = url)
        optimized.type = "image/gif"
        optimized.size = gifData.size
        optimized.width = posterImage.width
        optimized.height = posterImage.height
        this.optimized = optimized

        val hdpi = Attachment(url = url)
        hdpi.width = posterImage.width
        hdpi.height = posterImage.height
        hdpi.image = posterImage
        this.hdpi = hdpi
    }

    constructor(url: URL, image: Image) : this(id = UUIDString()) {
        val optimized = Attachment(url = url)
        optimized.width = image.width
        optimized.height = image.height
        optimized.image = image

        this.optimized = optimized
    }

    fun replace(type: AttachmentType, withAttachment: Attachment?) {
        when(type) {
            AttachmentType.Optimized -> optimized = withAttachment
            AttachmentType.Mdpi ->      mdpi = withAttachment
            AttachmentType.Hdpi ->      hdpi = withAttachment
            AttachmentType.Xhdpi ->     xhdpi = withAttachment
            AttachmentType.Original ->  original = withAttachment
            AttachmentType.Large ->     large = withAttachment
            AttachmentType.Regular ->   regular = withAttachment
        }
    }

    companion object {
        fun parseAsset(id: String, json: JSON?): Asset {
            val asset = Asset(id = id)
            val actualJson = json ?: return asset

            val attachments: List<Pair<String, AttachmentType>> = listOf(
                Pair("optimized", AttachmentType.Optimized),
                Pair("mdpi", AttachmentType.Mdpi),
                Pair("hdpi", AttachmentType.Hdpi),
                Pair("xhdpi", AttachmentType.Xhdpi),
                Pair("original", AttachmentType.Original),
                Pair("large", AttachmentType.Large),
                Pair("regular", AttachmentType.Regular)
            )
            for ((name, type) in attachments) {
                val attachmentJSON: JSON = actualJson[name]
                if (attachmentJSON["url"].string == null) continue

                val attachment = Attachment.fromJSON(json)
                asset.replace(type = type, withAttachment = attachment)
            }
            return asset
        }
    }

}
