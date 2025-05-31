package eu.haintech.hearbook.ui.addbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import eu.haintech.hearbook.R
import eu.haintech.hearbook.model.AppDatabase
import eu.haintech.hearbook.model.BookRepository
import kotlinx.coroutines.launch

class AddBookFragment : Fragment() {

    private val viewModel: AddBookViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        AddBookViewModel.Factory(BookRepository(database.bookDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleEditText = view.findViewById<EditText>(R.id.titleEditText)
        val authorEditText = view.findViewById<EditText>(R.id.authorEditText)
        val addButton = view.findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString().takeIf { it.isNotBlank() }
            val author = authorEditText.text.toString().takeIf { it.isNotBlank() }
            viewModel.addBook(title, author)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is AddBookViewModel.AddBookUiState.Success -> {
                            // Navigate to camera screen
                            val action = AddBookFragmentDirections.actionAddBookFragmentToCameraFragment(state.bookId)
                            findNavController().navigate(action)
                        }
                        is AddBookViewModel.AddBookUiState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                        }
                        AddBookViewModel.AddBookUiState.Initial -> {
                            // Initial state, nothing to do
                        }
                    }
                }
            }
        }
    }
} 