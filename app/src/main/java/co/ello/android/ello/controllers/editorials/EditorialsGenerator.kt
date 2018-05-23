package co.ello.android.ello


class EditorialsGenerator(val delegate: EditorialsProtocols.Controller?, val queue: Queue)
    : EditorialsProtocols.Generator
{

    override fun loadEditorialsStream() {
        API().editorialStream()
            .enqueue(queue)
            .onSuccess { (_, editorials) ->
                delegate?.loadedEditorialsStream(editorials)
            }
            // .onFailure { error ->
            // }
    }
}
