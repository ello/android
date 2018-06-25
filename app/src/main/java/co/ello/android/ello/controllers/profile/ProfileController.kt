package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class ProfileController(a: AppActivity) : StreamableController(a), ProfileProtocols.Controller {
    private lateinit var screen: ProfileProtocols.Screen
    private var generator: ProfileProtocols.Generator = ProfileGenerator(delegate = this, queue = requestQueue)

    override val viewForStream: ViewGroup get() = screen.streamContainer
    private lateinit var token: Token
    private var user: User? = null

    constructor(a: AppActivity, token: Token) : this(a) {
        this.token = token
    }

    constructor(a: AppActivity, user: User) : this(a) {
        this.token = ID(user.id)
        this.user = user
    }

    fun isShowing(user: User): Boolean {
        return token.matches(user.id, user.username)
    }

    override fun createView(): View {
        val screen = ProfileScreen(activity, delegate = this)
        this.screen = screen
        return screen.contentView
    }

    override fun onStart() {
        val placeholders = listOf(
            StreamCellItem(type = StreamCellType.Spinner, placeholderType = StreamCellType.PlaceholderType.Header),
            StreamCellItem(type = StreamCellType.Placeholder, placeholderType = StreamCellType.PlaceholderType.StreamItems)
            )
        streamController.replaceAll(placeholders)

        val user = this.user
        if (user?.hasProfileData != true) {
            generator.loadUser(token)
        }

        if (user != null) {
            loadedUser(user, generator.parseUser(user))
        }
    }

    override fun loadedUser(user: User, items: List<StreamCellItem>) {
        this.user = user
        streamController.replacePlaceholder(StreamCellType.PlaceholderType.Header, with = items)
        if (!streamController.hasPlaceholderItems(StreamCellType.PlaceholderType.StreamItems)) {
            streamController.replacePlaceholder(StreamCellType.PlaceholderType.StreamItems, with = listOf(StreamCellItem(type = StreamCellType.Spinner)))
        }
        generator.loadUserPosts(user.username)
    }

    override fun loadedUserPosts(items: List<StreamCellItem>) {
        streamController.replacePlaceholder(StreamCellType.PlaceholderType.StreamItems, items)
    }
}
