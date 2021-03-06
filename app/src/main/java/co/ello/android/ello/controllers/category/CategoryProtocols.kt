package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class CategoryProtocols {
    interface Screen {
        val contentView: View
        val streamContainer: ViewGroup
        fun updateSubscribedCategories(categoryInfo: List<CategoryScreen.CardInfo>)
        fun highlightSubscribedCategory(selectedInfo: CategoryScreen.CardInfo)
    }

    interface Controller {
        fun loadedSubscribedCategories(categories: List<Category>)
        fun loadedCategoryStream(items: List<StreamCellItem>)
        fun categorySelected(info: CategoryScreen.CardInfo)
    }

    interface Generator {
        fun loadSubscribedCategories()
        fun loadStream(filter: API.StreamFilter, stream: CategoryGenerator.Stream)
    }
}
