package co.ello.android.ello

import android.view.ViewGroup
import java.net.URL


sealed class StreamCellType {
    object Placeholder : StreamCellType()
    object Spacer : StreamCellType()

    object Spinner : StreamCellType()
    data class Error(val message: String) : StreamCellType()

    object StreamSelection : StreamCellType()
    object PostHeader : StreamCellType()
    object CommentHeader : StreamCellType()
    object PostFooter : StreamCellType()
    data class PostText(val content: String) : StreamCellType()
    data class CommentText(val content: String) : StreamCellType()
    data class PostImage(val imageURL: URL) : StreamCellType()

    object ProfileHeaderButtons : StreamCellType()
    object ProfileHeaderAvatar : StreamCellType()
    object ProfileHeaderName : StreamCellType()
    object ProfileHeaderTotalAndBadges : StreamCellType()
    object ProfileHeaderStats : StreamCellType()
    object ProfileHeaderBio : StreamCellType()
    object ProfileHeaderLocation : StreamCellType()
    object ProfileHeaderLinks : StreamCellType()
    object ProfileHeaderSeparator : StreamCellType()

    val viewType: Int get() = StreamCellType::class.nestedClasses.indexOf(this::class)

    fun bindViewHolder(viewHolder: StreamCell, item: StreamCellItem) {
        viewHolder.streamCellItem = item

        return when(this) {
            is Placeholder -> {}
            is Spacer -> {}

            is Spinner -> SpinnerPresenter.configure(viewHolder as SpinnerCell)
            is Error -> ErrorPresenter.configure(viewHolder as ErrorCell, item)

            is StreamSelection -> StreamSelectionPresenter.configure(viewHolder as StreamSelectionCell, item)
            is PostHeader -> PostHeaderPresenter.configure(viewHolder as PostHeaderCell, item)
            is CommentHeader -> CommentHeaderPresenter.configure(viewHolder as CommentHeaderCell, item)
            is PostFooter -> PostFooterPresenter.configure(viewHolder as PostFooterCell, item)
            is PostText -> PostTextPresenter.configure(viewHolder as PostTextCell, item)
            is CommentText -> PostTextPresenter.configure(viewHolder as PostTextCell, item)
            is PostImage -> PostImagePresenter.configure(viewHolder as PostImageCell, item)

            is ProfileHeaderButtons -> ProfileHeaderButtonsPresenter.configure(viewHolder as ProfileHeaderButtonsCell, item)
            is ProfileHeaderAvatar -> ProfileHeaderAvatarPresenter.configure(viewHolder as ProfileHeaderAvatarCell, item)
            is ProfileHeaderName -> ProfileHeaderNamePresenter.configure(viewHolder as ProfileHeaderNameCell, item)
            is ProfileHeaderTotalAndBadges -> ProfileHeaderTotalAndBadgesPresenter.configure(viewHolder as ProfileHeaderTotalAndBadgesCell, item)
            is ProfileHeaderStats -> ProfileHeaderStatsPresenter.configure(viewHolder as ProfileHeaderStatsCell, item)
            is ProfileHeaderBio -> ProfileHeaderBioPresenter.configure(viewHolder as ProfileHeaderBioCell, item)
            is ProfileHeaderLocation -> ProfileHeaderLocationPresenter.configure(viewHolder as ProfileHeaderLocationCell, item)
            is ProfileHeaderLinks -> ProfileHeaderLinksPresenter.configure(viewHolder as ProfileHeaderLinksCell, item)
            is ProfileHeaderSeparator -> {}
        }
    }

    companion object {
        fun createViewHolder(parent: ViewGroup, viewType: Int): StreamCell {
            val type = StreamCellType::class.nestedClasses.elementAt(viewType)
            return when (type) {
                Placeholder::class -> PlaceholderCell(parent)
                Spacer::class -> SpacerCell(parent)

                Spinner::class -> SpinnerCell(parent)
                Error::class -> ErrorCell(parent)

                StreamSelection::class -> StreamSelectionCell(parent)
                PostHeader::class -> PostHeaderCell(parent)
                CommentHeader::class -> CommentHeaderCell(parent)
                PostFooter::class -> PostFooterCell(parent)
                PostText::class -> PostTextCell(parent, isComment = false)
                CommentText::class -> PostTextCell(parent, isComment = true)
                PostImage::class -> PostImageCell(parent)

                ProfileHeaderButtons::class -> ProfileHeaderButtonsCell(parent)
                ProfileHeaderAvatar::class -> ProfileHeaderAvatarCell(parent)
                ProfileHeaderName::class -> ProfileHeaderNameCell(parent)
                ProfileHeaderTotalAndBadges::class -> ProfileHeaderTotalAndBadgesCell(parent)
                ProfileHeaderStats::class -> ProfileHeaderStatsCell(parent)
                ProfileHeaderBio::class -> ProfileHeaderBioCell(parent)
                ProfileHeaderLocation::class -> ProfileHeaderLocationCell(parent)
                ProfileHeaderLinks::class -> ProfileHeaderLinksCell(parent)
                ProfileHeaderSeparator::class -> ProfileHeaderSeparatorCell(parent)

                else -> throw IllegalArgumentException("Unhandled type $type")
            }
        }
    }

    enum class PlaceholderType {
        Header, Spinner, StreamFilter, StreamItems;
    }
}
