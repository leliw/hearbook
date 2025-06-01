package eu.haintech.hearbook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import eu.haintech.hearbook.R
import eu.haintech.hearbook.ui.viewmodels.AddBookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    onNavigateToScanning: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: AddBookViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiState) {
        viewModel.uiState.collect { state ->
            when (state) {
                is AddBookViewModel.AddBookUiState.Success -> {
                    onNavigateToScanning(state.bookId)
                }
                is AddBookViewModel.AddBookUiState.Error -> {
                    showError = state.message
                    snackbarHostState.showSnackbar(
                        message = state.message,
                        duration = SnackbarDuration.Long
                    )
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                modifier = Modifier.fillMaxWidth(),
                isError = showError != null
            )

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text(stringResource(R.string.book_author)) },
                modifier = Modifier.fillMaxWidth(),
                isError = showError != null
            )

            Button(
                onClick = { 
                    showError = null
                    viewModel.addBook(title.trim(), author.trim()) 
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
} 