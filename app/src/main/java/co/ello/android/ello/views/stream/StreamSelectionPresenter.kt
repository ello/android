package co.ello.android.ello


class StreamSelectionPresenter {
    companion object {
        fun configure(cell: StreamSelectionCell, item: StreamCellItem) {
            cell.selectedIndex = (item.extra as? Int) ?: 0
        }
    }
}
