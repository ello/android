package co.ello.android.ello


class JoinGenerator(val delegate: JoinProtocols.Controller?)
    : JoinProtocols.Generator
{

    override fun join(queue: Queue, email: String, username: String, password: String) {
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
