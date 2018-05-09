package co.ello.android.ello

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.net.URL


class CategoryScreen : CategoryProtocols.Screen {
    override val contentView: View
    override val streamContainer: ViewGroup
    val inflater: LayoutInflater
    val cardListContainer: LinearLayout

    override var delegate: CategoryProtocols.Controller? = null

    sealed class CardInfo(val title: String, val kind: Kind, val imageURL: URL?) {
        enum class Kind {
            All, Subscribed, ZeroState, Category
        }

        object All : CardInfo(title = "All", kind = Kind.All, imageURL = null)
        object Subscribed : CardInfo(title = "Subscribed", kind = Kind.Subscribed, imageURL = null)
        object ZeroState : CardInfo(title = "Choose communities to subscribe to here.", kind = Kind.ZeroState, imageURL = null)
        class Category(category: co.ello.android.ello.Category) : CardInfo(title = category.name, kind = Kind.Category, imageURL = category.tileURL)
    }

    constructor(activity: Activity) {
        inflater = activity.layoutInflater
        contentView = inflater.inflate(R.layout.category_layout, null)
        cardListContainer = contentView.findViewById<LinearLayout>(R.id.cardListContainer)
        streamContainer = contentView.findViewById<ViewGroup>(R.id.streamContainer)
    }

    override fun updateSubscribedCategories(categories: List<CardInfo>) {
        cardListContainer.removeAllViews()

        for (info in categories) {
            val cardLayout = inflater.inflate(R.layout.category_card_view, null)
            val label = cardLayout.findViewById<TextView>(R.id.label)
            val imageView = cardLayout.findViewById<ImageView>(R.id.imageView)
            label.setText(info.title)
            info.imageURL?.let { url ->
                Picasso.get().load(url.toString()).into(imageView)
            }

            val width = (when(info.kind) {
                CardInfo.Kind.All -> 50f
                CardInfo.Kind.ZeroState -> 300f
                CardInfo.Kind.Subscribed, CardInfo.Kind.Category-> 100f
            } * contentView.resources.displayMetrics.density).toInt()
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val layoutParams = LinearLayout.LayoutParams(width, height)
            cardLayout.layoutParams = layoutParams

            cardListContainer.addView(cardLayout)
        }
    }

}
