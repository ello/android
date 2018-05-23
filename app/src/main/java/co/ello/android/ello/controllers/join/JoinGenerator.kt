package co.ello.android.ello


class JoinGenerator(val delegate: JoinProtocols.Controller?, val queue: Queue)
    : JoinProtocols.Generator
{

    override fun join(email: String, username: String, password: String) {
        API().join(email, username, password)
                .enqueue(queue)
                .onSuccess { credentials ->
                    delegate?.success(credentials)
                }
                .onFailure {
                    delegate?.failure()
                }
    }
}
