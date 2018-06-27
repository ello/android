package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class CategoryController(a: AppActivity) : StreamableController(a), CategoryProtocols.Controller, StreamSelectionDelegate {
    private lateinit var screen: CategoryProtocols.Screen
    private var generator: CategoryProtocols.Generator = CategoryGenerator(delegate = this, queue = requestQueue)

    private var categoryInfo: List<CategoryScreen.CardInfo>? = null
    private var stream: CategoryGenerator.Stream = CategoryGenerator.Stream.All
    private var filter: API.StreamFilter = API.StreamFilter.Featured

    override val viewForStream: ViewGroup get() = screen.streamContainer

    init {
        streamController.streamSelectionDelegate = this
    }

    override fun createView(): View {
        val screen = CategoryScreen(activity, delegate = this)
        this.screen = screen
        categoryInfo?.let { screen.updateSubscribedCategories(it) }
        return screen.contentView
    }

    override fun onAppear() {
        if (categoryInfo == null) {
            streamController.replaceAll(listOf(StreamCellItem(type = StreamCellType.Spinner, placeholderType = StreamCellType.PlaceholderType.Spinner)))
        }
    }

    override fun onStart() {
        generator.loadSubscribedCategories()
        generator.loadStream(filter, stream)
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

    override fun loadedCategoryStream(items: List<StreamCellItem>) {
        if (!streamController.hasPlaceholderItems(StreamCellType.PlaceholderType.StreamFilter)) {
            val streamSelection = StreamCellItem(type = StreamCellType.StreamSelection)
            streamSelection.extra = 0
            streamController.replacePlaceholder(StreamCellType.PlaceholderType.StreamFilter, listOf(streamSelection))
        }

        streamController.replacePlaceholder(StreamCellType.PlaceholderType.Spinner, emptyList())
        streamController.replacePlaceholder(StreamCellType.PlaceholderType.StreamItems, items)
    }

    override fun categorySelected(info: CategoryScreen.CardInfo) {
        val stream = when(info) {
            is CategoryScreen.CardInfo.All -> CategoryGenerator.Stream.All
            is CategoryScreen.CardInfo.Subscribed -> CategoryGenerator.Stream.Subscribed
            is CategoryScreen.CardInfo.Category -> CategoryGenerator.Stream.Category(ID(info.category.id))
            else -> null
        } ?: return
        if (this.stream.equals(stream))  return

        this.stream = stream
        streamController.replaceAll(listOf(StreamCellItem(type = StreamCellType.Spinner, placeholderType = StreamCellType.PlaceholderType.Spinner)))
        generator.loadStream(filter, stream)
    }

    override fun streamSelectionChanged(index: Int) {
        filter = when(index) {
            1 -> API.StreamFilter.Trending
            2 -> API.StreamFilter.Recent
            3 -> API.StreamFilter.Shop
            else -> API.StreamFilter.Featured
        }

        streamController.replacePlaceholder(StreamCellType.PlaceholderType.StreamItems, listOf(StreamCellItem(type = StreamCellType.Spinner)))
        generator.loadStream(filter, stream)
    }

}
