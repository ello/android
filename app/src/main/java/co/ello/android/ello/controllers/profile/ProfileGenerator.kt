package co.ello.android.ello


class ProfileGenerator(val delegate: ProfileProtocols.Controller?, val queue: Queue)
    : ProfileProtocols.Generator
{

    override fun loadUser(token: Token) {
        API().userDetail(token)
            .enqueue(queue)
            .onSuccess { user ->
                delegate?.loadedUser(user)
            }
    }
}
