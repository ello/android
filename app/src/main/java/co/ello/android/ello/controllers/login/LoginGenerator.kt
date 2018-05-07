package co.ello.android.ello


class LoginGenerator : LoginProtocols.Generator {
    override var delegate: LoginProtocols.Controller? = null

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
