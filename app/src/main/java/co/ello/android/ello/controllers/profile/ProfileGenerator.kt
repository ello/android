package co.ello.android.ello

import java.util.UUID
import kotlinx.coroutines.launch
import kotlinx.coroutines.android.UI


class ProfileGenerator(val delegate: ProfileProtocols.Controller?, val queue: Queue)
    : ProfileProtocols.Generator
{
    var userToken: UUID? = null
    private fun newUUID(): UUID {
        return randomUUID().apply { userToken = this }
    }

    override fun loadUser(token: Token) {
        val currentToken = newUUID()
        launch(UI) exit@ {
            val result = API().userDetail(token).enqueue(queue)
            if (currentToken != userToken)  return@exit

            when (result) {
                is Success -> {
                    val user = result.value
                    delegate?.loadedUser(user, parseUser(user))
                }
                is Failure -> println("load user error: ${result.error}")
            }
        }
    }

    override fun parseUser(user: User): List<StreamCellItem> {
        return StreamParser().userProfileItems(user)
    }

    override fun loadUserPosts(username: String) {
        launch(UI) {
            val result = API().userPosts(username).enqueue(queue)
            when (result) {
                is Success -> {
                    val (_, posts) = result.value
                    val items = StreamParser().parse(posts)
                    delegate?.loadedUserPosts(items)
                }
                is Failure -> println("load user posts error: ${result.error}")
            }
        }
    }
}
