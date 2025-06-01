package eu.haintech.hearbook.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.haintech.hearbook.model.Book
import eu.haintech.hearbook.model.BookRepository
import eu.haintech.hearbook.utils.FileManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val repository: BookRepository,
    private val fileManager: FileManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val books: StateFlow<List<Book>> = repository.allBooks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            // Usuń pliki skanów
            try {
                val bookDir = fileManager.getBookDirectory(context, book.id)
                if (bookDir.exists() && bookDir.isDirectory) {
                    bookDir.deleteRecursively()
                }
            } catch (e: Exception) {
                // Ignorujemy błędy usuwania plików - najważniejsze jest usunięcie z bazy danych
            }

            // Usuń książkę z bazy danych
            repository.delete(book)
        }
    }
} 