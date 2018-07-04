package co.ello.android.ello


object ErrorPresenter {
    fun configure(cell: ErrorCell, item: StreamCellItem) {
        val data = item.type as? StreamCellType.Error ?: return

        cell.config(ErrorCell.Config(
            message = data.message
            ))
    }
}
