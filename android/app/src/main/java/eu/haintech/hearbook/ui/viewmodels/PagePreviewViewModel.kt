package eu.haintech.hearbook.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.haintech.hearbook.model.Book
import eu.haintech.hearbook.model.BookRepository
import eu.haintech.hearbook.utils.FileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PagePreviewViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val fileManager: FileManager
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    private val _pages = MutableStateFlow<List<File>>(emptyList())
    val pages: StateFlow<List<File>> = _pages

    fun loadBook(bookId: Long, context: Context) {
        viewModelScope.launch {
            bookRepository.getBook(bookId)?.let { book ->
                _book.value = book
                loadPages(bookId, context)
            }
        }
    }

    private fun loadPages(bookId: Long, context: Context) {
        _pages.value = fileManager.getBookPages(context, bookId)
    }
} 