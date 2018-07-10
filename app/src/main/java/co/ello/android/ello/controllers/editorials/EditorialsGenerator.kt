package co.ello.android.ello

import java.util.UUID
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.android.UI


class EditorialsGenerator(val delegate: EditorialsProtocols.Controller?, val queue: Queue)
    : EditorialsProtocols.Generator
{

    var streamToken: UUID? = null
    private fun newUUID(): UUID {
        return randomUUID().apply { streamToken = this }
    }

    override fun loadEditorialsStream() {
        val currentToken = newUUID()
        launch(UI) exit@ {
            val result = API().editorialStream().enqueue(queue)
            if (currentToken != streamToken)  return@exit
            when (result) {
                is Success -> {
                    val (_, editorials) = result.value
                    val items = StreamParser().parse(editorials)
                    delegate?.loadedEditorialsStream(items)
                }
                is Failure -> println("editorials fail: ${result.error}")
            }
        }
    }
}
