package co.ello.android.ello


class CategoryGenerator(val delegate: CategoryProtocols.Controller?)
    : CategoryProtocols.Generator
{
    override fun loadSubscribedCategories(queue: Queue) {
        API().subscribedCategories()
            .enqueue(queue)
            .onSuccess { subscribedCategories ->
                delegate?.loadedSubscribedCategories(subscribedCategories)
            }
            .onFailure { error ->
                println("subscribedCategories fail: $error")
            }
    }

    override fun loadCategoryStream(queue: Queue) {
        API().globalPostStream(API.CategoryFilter.Featured)
            .enqueue(queue)
            .onSuccess { (_, posts) ->
                delegate?.loadedCategoryStream(posts)
            }
            .onFailure { error ->
                println("globalPostStream fail: $error")
            }
    }
}
