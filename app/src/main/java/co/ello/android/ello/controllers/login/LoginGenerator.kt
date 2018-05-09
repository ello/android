package co.ello.android.ello


class LoginGenerator(val delegate: LoginProtocols.Controller?)
    : LoginProtocols.Generator
{

    override fun login(queue: Queue, username: String, password: String) {
        API().login(username, password)
            .enqueue(queue)
            .onSuccess { credentials ->
                credentials.isAnonymous = false
                delegate?.success(credentials)
            }
            .onFailure {
                delegate?.failure()
            }
    }
}
