package eu.haintech.hearbook.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import eu.haintech.hearbook.model.AppDatabase
import eu.haintech.hearbook.model.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository
    private val _uiState = MutableStateFlow<AddBookUiState>(AddBookUiState.Initial)
    val uiState: StateFlow<AddBookUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = BookRepository(database.bookDao())
    }

    fun addBook(title: String?, author: String?) {
        viewModelScope.launch {
            try {
                val bookId = repository.insert(title, author)
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