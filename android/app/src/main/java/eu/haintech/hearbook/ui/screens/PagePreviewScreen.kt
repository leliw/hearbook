package eu.haintech.hearbook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eu.haintech.hearbook.R
import eu.haintech.hearbook.model.Book
import eu.haintech.hearbook.ui.viewmodels.PagePreviewViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagePreviewScreen(
    bookId: Long,
    navController: NavController,
    viewModel: PagePreviewViewModel = hiltViewModel()
) {
    val book by viewModel.book.collectAsState()
    val pages by viewModel.pages.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId, context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = book?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("scanning/${bookId}")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_page)
                )
            }
        }
    ) { paddingValues ->
        if (pages.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.no_pages_found))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            ) {
                items(pages) { page ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .aspectRatio(0.7f),
                        onClick = { /* Możliwość powiększenia strony */ }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(page)
                                    .build()
                            ),
                            contentDescription = stringResource(R.string.page_scan),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
} 