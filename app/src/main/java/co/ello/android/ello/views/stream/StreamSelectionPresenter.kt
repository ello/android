package co.ello.android.ello


object StreamSelectionPresenter {
    fun configure(cell: StreamSelectionCell, item: StreamCellItem) {
        cell.selectedIndex = (item.extra as? Int) ?: 0
    }
}
