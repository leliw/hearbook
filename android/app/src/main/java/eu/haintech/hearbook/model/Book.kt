package eu.haintech.hearbook.model

enum class BookStatus {
    SCANNING,
    PROCESSING,
    READY_TO_READ
}

data class Book(
    val id: String,
    val title: String,
    val status: BookStatus,
    val coverUrl: String? = null,
    val pageCount: Int = 0,
    val currentPage: Int = 0,
    val readingProgress: Float = 0f
) 