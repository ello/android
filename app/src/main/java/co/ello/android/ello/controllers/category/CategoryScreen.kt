package co.ello.android.ello

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import java.net.URL


class CategoryScreen : CategoryProtocols.Screen {
    private val delegate: CategoryProtocols.Controller?

    override val contentView: View
    override val streamContainer: ViewGroup
    val cardListContainer: LinearLayout
    private var categoryCards: List<CardHolder> = emptyList()

    constructor(activity: Activity, delegate: CategoryProtocols.Controller?) {
        this.delegate = delegate

        contentView = activity.layoutInflater.inflate(R.layout.category_layout, null)
        cardListContainer = contentView.findViewById(R.id.cardListContainer)
        streamContainer = contentView.findViewById(R.id.streamContainer)
    }

    sealed class CardInfo(val title: StringRes, val kind: Kind, val imageURL: URL?) {
        enum class Kind {
            All, Subscribed, ZeroState, Category
        }

        object All : CardInfo(title = Res(R.string.Category_all), kind = Kind.All, imageURL = null)
        object Subscribed : CardInfo(title = Res(R.string.Category_subscribed), kind = Kind.Subscribed, imageURL = null)
        object ZeroState : CardInfo(title = Res(R.string.Category_zeroState), kind = Kind.ZeroState, imageURL = null)
        class Category(val category: co.ello.android.ello.Category) : CardInfo(title = Lit(category.name), kind = Kind.Category, imageURL = category.tileURL)

        override fun equals(other: Any?): Boolean {
            if (other == null)  return false
            if (other !is CardInfo)  return false
            if (other is All && this is All)  return true
            if (other is Subscribed && this is Subscribed)  return true
            if (other is ZeroState && this is ZeroState)  return true
            if (other is Category && this is Category)  return other.category.id == this.category.id
            return false
        }
    }

    data class CardHolder(val info: CardInfo, val view: View) {
        val label = view.findViewById<StyledLabel>(R.id.label)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val overlay = view.findViewById<View>(R.id.overlay)
    }

    override fun updateSubscribedCategories(categoryInfo: List<CardInfo>) {
        cardListContainer.removeAllViews()

        val inflater = LayoutInflater.from(contentView.context)
        this.categoryCards = categoryInfo.map { info ->
            val view = inflater.inflate(R.layout.category_card_view, cardListContainer, false)
            val cardHolder = CardHolder(info, view)
            cardHolder.label.setText(info.title.gen())
            cardHolder.label.style = if (info == CardInfo.All) StyledLabel.Style.BoldWhiteUnderline else StyledLabel.Style.White
            cardHolder.imageView.setImageURL(info.imageURL)
            cardHolder.imageView.setOnClickListener { categoryCardTapped(info) }
            cardHolder.overlay.alpha = if (info == CardInfo.All) 0.8f else 0.5f

            cardListContainer.addView(view)

            val width = (when(info.kind) {
                CardInfo.Kind.All -> 50
                CardInfo.Kind.ZeroState -> 300
                CardInfo.Kind.Subscribed, CardInfo.Kind.Category-> 100
            }).dp
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val layoutParams = LinearLayout.LayoutParams(width, height)
            view.layoutParams = layoutParams

            cardHolder
        }
    }

    override fun highlightSubscribedCategory(selectedInfo: CardInfo) {
        for (cardHolder in categoryCards) {
            cardHolder.overlay.alpha = if (cardHolder.info == selectedInfo) 0.8f else 0.5f
        }
    }

    private fun categoryCardTapped(info: CardInfo) {
        highlightSubscribedCategory(info)
        delegate?.categorySelected(info)
    }

}
