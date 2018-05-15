package co.ello.android.ello


typealias Block = (() -> Unit)
typealias CompletionBlock = ((Boolean) -> Unit)


class DispatchQueue {
    fun async(execute: Block) = execute()

    companion object {
        val main = DispatchQueue()
    }
}
