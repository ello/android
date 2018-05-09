package co.ello.android.ello


class StreamParser {
    fun parse(models: List<Model>): List<StreamCellItem> {
        return models.flatMap { model ->
            if (model is Post) {
                listOf(StreamCellItem(model = model, type = StreamCellType.PostHeader))
            }
            else emptyList()
        }
    }
}
