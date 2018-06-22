package co.ello.android.ello


class LoginGenerator(val delegate: LoginProtocols.Controller?, val queue: Queue)
    : LoginProtocols.Generator
{

    override fun login(username: String, password: String) {
        API().login(username, password)
            .enqueue(queue)
            .onSuccess { credentials ->
                delegate?.success(credentials)
            }
            .onFailure {
                delegate?.failure()
            }
    }
}
