object BookmarksObj {
    private val bookmarkList = mutableListOf<String>()

    fun getBookmarks(): List<String> {
        return bookmarkList
    }

    fun setBookmarks(bookmarks: List<String>) {
        bookmarkList.clear()
        bookmarkList.addAll(bookmarks)
    }

    fun addBookmark(bookmarkId: String) {
        if (!bookmarkList.contains(bookmarkId)) {
            bookmarkList.add(bookmarkId)
        }
    }

    fun removeBookmark(bookmarkId: String) {
        bookmarkList.remove(bookmarkId)
    }
}
