package co.ello.android.ello.stream

interface ScrollListener {

    fun onScrollPointReached()

    fun isScrollPointReached(scrollPoint: Double, itemCount: Int, position: Int) : Boolean {
        if (!isScrollAvailable(itemCount)) return false
        val scrollItem = (scrollPoint * itemCount).toInt()
        return position == scrollItem - 1
    }

    fun isScrollAvailable(itemCount: Int) : Boolean {
        return itemCount > 1
    }
}