package eu.haintech.hearbook.ui.addbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eu.haintech.hearbook.model.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBookViewModel(private val repository: BookRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AddBookUiState>(AddBookUiState.Initial)
    val uiState: StateFlow<AddBookUiState> = _uiState.asStateFlow()

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

    class Factory(private val repository: BookRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddBookViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddBookViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 