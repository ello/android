package co.ello.android.ello


class LoginGenerator : LoginProtocols.Generator {
    override var delegate: LoginProtocols.Controller? = null

    override fun login(queue: Queue, username: String, password: String) {
        API(queue).login(username, password)
            .onSuccess { credentials ->
                delegate?.success(credentials)
            }
            .onFailure {
                delegate?.failure()
            }
    }
}
