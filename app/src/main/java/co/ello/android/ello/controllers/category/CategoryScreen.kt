package co.ello.android.ello

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.net.URL


class CategoryScreen : CategoryProtocols.Screen {
    override val contentView: View
    override val streamContainer: ViewGroup
    val cardListContainer: LinearLayout

    override var delegate: CategoryProtocols.Controller? = null

    sealed class CardInfo(val title: String, val kind: Kind, val imageURL: URL?) {
        enum class Kind {
            All, Subscribed, ZeroState, Category
        }

        object All : CardInfo(title = T(R.string.Category_All), kind = Kind.All, imageURL = null)
        object Subscribed : CardInfo(title = T(R.string.Category_Subscribed), kind = Kind.Subscribed, imageURL = null)
        object ZeroState : CardInfo(title = T(R.string.Category_ZeroState), kind = Kind.ZeroState, imageURL = null)
        class Category(category: co.ello.android.ello.Category) : CardInfo(title = category.name, kind = Kind.Category, imageURL = category.tileURL)
    }

    constructor(activity: Activity) {
        contentView = activity.layoutInflater.inflate(R.layout.category_layout, null)
        cardListContainer = contentView.findViewById(R.id.cardListContainer)
        streamContainer = contentView.findViewById(R.id.streamContainer)
    }

    override fun updateSubscribedCategories(categories: List<CardInfo>) {
        cardListContainer.removeAllViews()

        for (info in categories) {
            val cardLayout = LayoutInflater.from(contentView.context).inflate(R.layout.category_card_view, null)
            val label = cardLayout.findViewById<TextView>(R.id.label)
            val imageView = cardLayout.findViewById<ImageView>(R.id.imageView)
            label.setText(info.title)
            imageView.setImageURL(info.imageURL)

            val width = (when(info.kind) {
                CardInfo.Kind.All -> 50
                CardInfo.Kind.ZeroState -> 300
                CardInfo.Kind.Subscribed, CardInfo.Kind.Category-> 100
            }).dp
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val layoutParams = LinearLayout.LayoutParams(width, height)
            cardLayout.layoutParams = layoutParams

            cardListContainer.addView(cardLayout)
        }
    }

}
