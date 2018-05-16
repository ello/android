package co.ello.android.ello


class StreamParser {
    fun parse(models: List<Model>): List<StreamCellItem> {
        return models.flatMap { model ->
            if (model is Post) {
                val header = StreamCellItem(model = model, type = StreamCellType.PostHeader)
                val footer = StreamCellItem(model = model, type = StreamCellType.PostFooter)
                val content = if (model.isRepost)
                    model.repostContent.flatMap { parseContent(model, it) } +
                    model.content.flatMap { parseContent(model, it) }
                else
                    model.content.flatMap { parseContent(model, it) }

                listOf(header) + content + listOf(footer)
            }
            else if (model is Editorial) {
                emptyList()
            }
            else emptyList()
        }
    }

    private fun parseContent(model: Model, region: Regionable): List<StreamCellItem> {
        if (region is TextRegion) {
            return listOf(StreamCellItem(model = model, type = StreamCellType.PostText(region.content)))
        }
        else if (region is ImageRegion) {
            region.asset?.oneColumnAttachment?.url?.let {
                return listOf(StreamCellItem(model = model, type = StreamCellType.PostImage(it)))
            }
        }
        return emptyList()
    }
}
