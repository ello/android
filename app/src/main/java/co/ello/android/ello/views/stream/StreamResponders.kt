package co.ello.android.ello


interface StreamResponder {
    fun streamTappedPost(cell: StreamCell, item: StreamCellItem, post: Post)
    fun streamTappedUser(cell: StreamCell, item: StreamCellItem, user: User)
}

interface PostToolbarResponder {
    fun toolbarTappedLoves(cell: StreamCell, item: StreamCellItem)
    fun toolbarTappedComments(cell: StreamCell, item: StreamCellItem)
    fun toolbarTappedRepost(cell: StreamCell, item: StreamCellItem)
    fun toolbarTappedShare(cell: StreamCell, item: StreamCellItem)
}
