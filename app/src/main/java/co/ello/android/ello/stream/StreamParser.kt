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
