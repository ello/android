package co.ello.android.ello


class JoinGenerator : JoinProtocols.Generator {
    override var delegate: JoinProtocols.Controller? = null

    override fun join(queue: Queue, email: String, username: String, password: String) {
        API(queue).join(email, username, password)
            .onSuccess { credentials ->
                delegate?.success(credentials)
            }
            .onFailure {
                delegate?.failure()
            }
    }
}
