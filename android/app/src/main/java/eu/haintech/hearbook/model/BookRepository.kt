package eu.haintech.hearbook.model

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepository @Inject constructor(private val bookDao: BookDao) {
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()

    suspend fun getBook(bookId: Long): Book? {
        return bookDao.getBook(bookId)
    }

    suspend fun insert(title: String?, author: String?): Long {
        val bookTitle = title ?: run {
            val count = bookDao.getBookCount()
            "Książka ${count + 1}"
        }
        return bookDao.insertBook(Book(
            title = bookTitle,
            author = author
        ))
    }

    suspend fun update(book: Book) {
        bookDao.updateBook(book)
    }

    suspend fun delete(book: Book) {
        bookDao.deleteBook(book)
    }
} 