package co.ello.android.ello


class StreamParser {
    fun parse(models: List<Model>): List<StreamCellItem> {
        val items = models.flatMap { model ->
            if (model is Post) {
                postItems(model)
            }
            else if (model is Editorial) {
                emptyList()
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

    private fun parseContent(model: Model, region: Regionable): List<StreamCellItem> {
        if (region is TextRegion) {
            return listOf(StreamCellItem(model = model, type = StreamCellType.PostText(region.content)))
        }
        else if (region is ImageRegion) {
            return region.asset?.oneColumnAttachment?.url?.let {
                listOf(StreamCellItem(model = model, type = StreamCellType.PostImage(it)))
            } ?: emptyList()
        }
        return emptyList()
    }
}
