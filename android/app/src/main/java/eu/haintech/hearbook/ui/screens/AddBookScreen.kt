package eu.haintech.hearbook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.haintech.hearbook.R
import eu.haintech.hearbook.ui.viewmodels.AddBookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    onNavigateToScanning: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: AddBookViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.uiState) {
        viewModel.uiState.collect { state ->
            when (state) {
                is AddBookViewModel.AddBookUiState.Success -> {
                    onNavigateToScanning(state.bookId)
                }
                is AddBookViewModel.AddBookUiState.Error -> {
                    // TODO: Show error message
                }
                AddBookViewModel.AddBookUiState.Initial -> {
                    // Initial state, nothing to do
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_book)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.book_title)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text(stringResource(R.string.book_author)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.addBook(title.trim(), author.trim()) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
} 