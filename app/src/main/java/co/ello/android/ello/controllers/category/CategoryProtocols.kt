package co.ello.android.ello

import android.view.View
import android.view.ViewGroup


class CategoryProtocols {
    interface Screen {
        var delegate: Controller?
        val contentView: View
        val streamContainer: ViewGroup
        fun updateSubscribedCategories(categories: List<CategoryScreen.CardInfo>)
    }

    interface Controller {
        fun loadedSubscribedCategories(categories: List<Category>)
        fun loadedCategoryStream(posts: List<Post>)
        fun categorySelected(info: CategoryScreen.CardInfo)
    }

    interface Generator {
        fun loadSubscribedCategories()
        fun loadStream(stream: CategoryGenerator.Stream)
    }
}
