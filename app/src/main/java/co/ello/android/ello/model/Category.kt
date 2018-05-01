package co.ello.android.ello

import java.util.Date
import java.net.URL


data class Category(
    val id: String,
    val name: String,
    val slug: String,
    val order: Int,
    val allowInOnboarding: Boolean,
    val isCreatorType: Boolean,
    val level: CategoryLevel
    ) : Model() {

    val isMeta: Boolean get() = level == CategoryLevel.Meta
    val tileURL: URL? get() = tileImage?.url
    val tileImage: Attachment? = null
    val visibleOnSeeMore: Boolean get() = level == CategoryLevel.Primary || level == CategoryLevel.Secondary

    sealed class Selection {
        object All : Selection()
        object Subscribe : Selection()
        data class Category(val slug: String) : Selection()

        val shareLink: URL? get() = when(this) {
            is All -> URL("${Globals.baseURL}/discover")
            is Subscribe -> URL("${Globals.baseURL}/discover/subscribed")
            is Category -> URL("${Globals.baseURL}/discover/${this.slug}")
        }
    }


}
