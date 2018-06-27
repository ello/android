package co.ello.android.ello


class ProfileGenerator(val delegate: ProfileProtocols.Controller?, val queue: Queue)
    : ProfileProtocols.Generator
{

    override fun loadUser(token: Token) {
        API().userDetail(token)
            .enqueue(queue)
            .onSuccess { user ->
                delegate?.loadedUser(user, parseUser(user))
            }
    }

    override fun parseUser(user: User): List<StreamCellItem> {
        return StreamParser().userProfileItems(user)
    }

    override fun loadUserPosts(username: String) {
        API().userPosts(username)
            .enqueue(queue)
            .onSuccess { (config, posts) ->
                val items = StreamParser().parse(posts)
                delegate?.loadedUserPosts(items)
            }
    }
}
