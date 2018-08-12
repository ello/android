package co.ello.android.ello

import android.graphics.Region

class StreamParser {
    fun parse(models: List<Model>): List<StreamCellItem> {
        val items = models.flatMap { model ->
            if (model is Post) {
                postItems(model)
            }
            else if (model is Comment) {
                commentItems(model)
            }
            else if (model is Editorial) {
                emptyList()
            }
            else if (model is Notification) {
                notificationItems(model)
            }
            else emptyList()
        }
        return items
    }

    fun userProfileItems(user: User): List<StreamCellItem> {
        val items = mutableListOf(
            StreamCellItem(model = user, type = StreamCellType.ProfileHeaderButtons),
            StreamCellItem(model = user, type = StreamCellType.ProfileHeaderAvatar),
            StreamCellItem(model = user, type = StreamCellType.ProfileHeaderName)
            )

        if (user.badges.size > 0 ||
            user.hasProfileData && user.totalViewsCount?.let({ it > 0 }) == true)
        {
            items.add(StreamCellItem(type = StreamCellType.ProfileHeaderSeparator))
            items.add(StreamCellItem(model = user, type = StreamCellType.ProfileHeaderTotalAndBadges))
        }

        items.add(StreamCellItem(type = StreamCellType.ProfileHeaderSeparator))
        items.add(StreamCellItem(model = user, type = StreamCellType.ProfileHeaderStats))

        if (user.formattedShortBio?.isEmpty() == false) {
            items.add(StreamCellItem(type = StreamCellType.ProfileHeaderSeparator))
            items.add(StreamCellItem(model = user, type = StreamCellType.ProfileHeaderBio))
        }

        if (user.location?.isEmpty() == false) {
            items.add(StreamCellItem(type = StreamCellType.ProfileHeaderSeparator))
            items.add(StreamCellItem(model = user, type = StreamCellType.ProfileHeaderLocation))
        }

        if (user.externalLinksList?.size?.let({ it > 0 }) == true) {
            items.add(StreamCellItem(type = StreamCellType.ProfileHeaderSeparator))
            items.add(StreamCellItem(model = user, type = StreamCellType.ProfileHeaderLinks))
        }
        return items
    }

    private fun postItems(model: Post): List<StreamCellItem> {
        val header = StreamCellItem(model = model, type = StreamCellType.PostHeader)
        val footer = StreamCellItem(model = model, type = StreamCellType.PostFooter)
        val content = if (model.isRepost) {
            model.repostContent.flatMap { parseContent(model, it) } +
            model.content.flatMap { parseContent(model, it) }
        } else {
            model.content.flatMap { parseContent(model, it) }
        }
        return listOf(header) + content + listOf(footer)
    }

    private fun commentItems(model: Comment): List<StreamCellItem> {
        val header = StreamCellItem(model = model, type = StreamCellType.CommentHeader)
        val content = model.content.flatMap { parseContent(model, it) }
        return listOf(header) + content
    }

    private fun notificationItems(model: Notification) :List<StreamCellItem> {
        val header = StreamCellItem(model = model, type = StreamCellType.NotificationHeader)
        val footer = StreamCellItem(model = model, type = StreamCellType.NotificationFooter)
        val content = parseContent(model, model.textRegion as? Regionable)
        return listOf(header) + content + listOf(footer)
    }

    private fun parseContent(model: Model, region: Regionable?): List<StreamCellItem> {
        if (region !is Regionable) return emptyList()
        if (region is TextRegion) {
            val cellType: StreamCellType
            if (model is Comment) {
                cellType = StreamCellType.CommentText(region.content)
            }
            else if (model is Notification) {
                val textContent = (model.textRegion as TextRegion).content
                if (model.hasImage) {
                    val imageContent = (model.imageRegion as ImageRegion)
                    cellType = StreamCellType.NotificationImageText(textContent, imageContent)
                }
                else {
                    cellType = StreamCellType.NotificationText(textContent)
                }
            }
            else {
                cellType = StreamCellType.PostText(region.content)
            }
            return listOf(StreamCellItem(model = model, type = cellType))
        }
        else if (region is ImageRegion) {
            val cellType: StreamCellType
            if (model is Comment) {
                cellType = StreamCellType.CommentImage(region)
            }
            else {
                cellType = StreamCellType.PostImage(region)
            }
            return listOf(StreamCellItem(model = model, type = cellType))
        }
        else if (region is EmbedRegion) {
            val cellType: StreamCellType
            if (model is Comment) {
                cellType = StreamCellType.CommentEmbed(region)
            }
            else {
                cellType = StreamCellType.PostEmbed(region)
            }
            return listOf(StreamCellItem(model = model, type = cellType))
        }
        return emptyList()
    }
}
