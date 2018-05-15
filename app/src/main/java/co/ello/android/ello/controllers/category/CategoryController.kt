package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class CategoryController(a: AppActivity) : StreamableController(a), CategoryProtocols.Controller {
    private lateinit var screen: CategoryProtocols.Screen
    private val generator: CategoryProtocols.Generator = CategoryGenerator(delegate = this)

    private var categoryInfo: List<CategoryScreen.CardInfo>? = null

    override val viewForStream: ViewGroup get() = screen.streamContainer

    override fun createView(): View {
        val screen = CategoryScreen(activity)
        screen.delegate = this
        this.screen = screen
        categoryInfo?.let { screen.updateSubscribedCategories(it) }
        return screen.contentView
    }

    override fun onStart() {
        generator.loadSubscribedCategories(requestQueue)
        generator.loadCategoryStream(requestQueue)
    }

    override fun loadedCategoryStream(posts: List<Post>) {
        val items = StreamParser().parse(posts)
        streamController.addAll(items)
    }

    override fun loadedSubscribedCategories(categories: List<Category>) {
        val categoryInfo: MutableList<CategoryScreen.CardInfo> = mutableListOf(CategoryScreen.CardInfo.All)
        if (isLoggedIn) {
            categoryInfo.add(CategoryScreen.CardInfo.Subscribed)
        }
        categoryInfo.addAll(categories.map { CategoryScreen.CardInfo.Category(it) })
        this.categoryInfo = categoryInfo

        if (isViewLoaded)
            screen.updateSubscribedCategories(categoryInfo)
    }
}
