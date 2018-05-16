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
    }

    interface Generator {
        fun loadSubscribedCategories(queue: Queue)
        fun loadCategoryStream(queue: Queue)
    }
}
