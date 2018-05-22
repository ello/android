package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class CategoryController(a: AppActivity) : StreamableController(a), CategoryProtocols.Controller {
    private lateinit var screen: CategoryProtocols.Screen
    private var generator: CategoryProtocols.Generator = CategoryGenerator(delegate = this, queue = requestQueue)

    private var categoryInfo: List<CategoryScreen.CardInfo>? = null

    override val viewForStream: ViewGroup get() = screen.streamContainer

    override fun createView(): View {
        val screen = CategoryScreen(activity)
        screen.delegate = this
        this.screen = screen
        categoryInfo?.let { screen.updateSubscribedCategories(it) }
        return screen.contentView
    }

    override fun onAppear() {
        if (categoryInfo == null) {
            streamController.replaceAll(listOf(StreamCellItem(type = StreamCellType.Spinner)))
        }
    }

    override fun onStart() {
        generator.loadSubscribedCategories()
        generator.loadStream(CategoryGenerator.Stream.All)
    }

    override fun loadedCategoryStream(posts: List<Post>) {
        val items = StreamParser().parse(posts)
        streamController.replaceAll(items)
    }

    override fun loadedSubscribedCategories(categories: List<Category>) {
        val categoryInfo: MutableList<CategoryScreen.CardInfo> = mutableListOf(CategoryScreen.CardInfo.All)
        if (isLoggedIn) {
            categoryInfo.add(CategoryScreen.CardInfo.Subscribed)
        }
        categoryInfo.addAll(categories.map { CategoryScreen.CardInfo.Category(it) })
        this.categoryInfo = categoryInfo

        if (isViewLoaded) {
            screen.updateSubscribedCategories(categoryInfo)
        }
    }

    override fun categorySelected(info: CategoryScreen.CardInfo) {
        val stream = when(info) {
            is CategoryScreen.CardInfo.All -> CategoryGenerator.Stream.All
            is CategoryScreen.CardInfo.Subscribed -> CategoryGenerator.Stream.Subscribed
            is CategoryScreen.CardInfo.Category -> CategoryGenerator.Stream.Category(ID(info.category.id))
            else -> null
        } ?: return

        println("=============== CategoryController.kt at line 46 ===============")
        println("stream: ${stream}")
        streamController.replaceAll(listOf(StreamCellItem(type = StreamCellType.Spinner)))
        generator.loadStream(stream)
    }
}
