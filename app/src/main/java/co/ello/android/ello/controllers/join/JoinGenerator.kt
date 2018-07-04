package co.ello.android.ello

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.android.UI


class JoinGenerator(val delegate: JoinProtocols.Controller?, val queue: Queue)
    : JoinProtocols.Generator
{
    override fun join(email: String, username: String, password: String) {
        launch(UI) {
            val result = API().join(email, username, password).enqueue(queue)
            when(result) {
                is Success -> delegate?.success(result.value)
                is Failure -> delegate?.failure()
            }
        }
    }
}
