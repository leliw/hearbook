package eu.haintech.hearbook.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.haintech.hearbook.model.BookRepository
import eu.haintech.hearbook.utils.FileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val repository: BookRepository,
    private val fileManager: FileManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow<AddBookUiState>(AddBookUiState.Initial)
    val uiState: StateFlow<AddBookUiState> = _uiState.asStateFlow()

    fun addBook(title: String, author: String) {
        viewModelScope.launch {
            try {
                val bookId = repository.insert(
                    title = title.ifBlank { null },
                    author = author.ifBlank { null }
                )
                // Create scan directory for the book
                fileManager.getBookPath(context, bookId).also { path ->
                    java.io.File(path).mkdirs()
                }
                _uiState.value = AddBookUiState.Success(bookId)
            } catch (e: Exception) {
                _uiState.value = AddBookUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class AddBookUiState {
        object Initial : AddBookUiState()
        data class Success(val bookId: Long) : AddBookUiState()
        data class Error(val message: String) : AddBookUiState()
    }
} 