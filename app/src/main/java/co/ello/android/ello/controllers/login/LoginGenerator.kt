package co.ello.android.ello

import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch


class LoginGenerator(val delegate: LoginProtocols.Controller?, val queue: Queue)
    : LoginProtocols.Generator
{

    override fun login(username: String, password: String) {
        launch(UI) {
            val result = API().login(username, password).enqueue(queue)
            when (result) {
                is Success -> delegate?.success(result.value)
                is Failure -> {
                    val reason = (result.error as? NetworkError)?.reason
                    delegate?.failure(reason)
                }
            }
        }
    }
}
