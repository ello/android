package co.ello.android.ello


class EditorialsGenerator(val delegate: EditorialsProtocols.Controller?)
    : EditorialsProtocols.Generator
{

    override fun loadEditorialsStream(queue: Queue) {
        API().editorialStream()
            .enqueue(queue)
            .onSuccess { (_, editorials) ->
                delegate?.loadedEditorialsStream(editorials)
            }
            // .onFailure { error ->
            // }
    }
}
