package co.ello.android.ello


interface PostToolbarResponder {
    fun toolbarTappedViews(cell: StreamCell, item: StreamCellItem)
    fun toolbarTappedLoves(cell: StreamCell, item: StreamCellItem)
    fun toolbarTappedComments(cell: StreamCell, item: StreamCellItem)
    fun toolbarTappedRepost(cell: StreamCell, item: StreamCellItem)
    fun toolbarTappedShare(cell: StreamCell, item: StreamCellItem)
}
