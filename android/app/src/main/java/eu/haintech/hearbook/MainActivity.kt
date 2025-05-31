package eu.haintech.hearbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.haintech.hearbook.model.AppDatabase
import eu.haintech.hearbook.model.BookRepository
import eu.haintech.hearbook.model.BookStatus
import eu.haintech.hearbook.ui.screens.AddBookScreen
import eu.haintech.hearbook.ui.screens.BookListScreen
import eu.haintech.hearbook.ui.screens.ReadingScreen
import eu.haintech.hearbook.ui.screens.ScanningScreen
import eu.haintech.hearbook.ui.theme.HearBookTheme
import eu.haintech.hearbook.ui.viewmodel.BookListViewModel

class MainActivity : ComponentActivity() {
    private lateinit var repository: BookRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        repository = BookRepository(database.bookDao())
        
        setContent {
            HearBookTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HearBookApp(repository)
                }
            }
        }
    }
}

@Composable
fun HearBookApp(repository: BookRepository) {
    val navController = rememberNavController()
    var pageCount by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    
    val bookListViewModel = viewModel<BookListViewModel>(
        factory = BookListViewModel.Factory(repository)
    )
    val books by bookListViewModel.books.collectAsState()

    NavHost(navController = navController, startDestination = "books") {
        composable("books") {
            BookListScreen(
                viewModel = bookListViewModel,
                onAddBookClick = { 
                    navController.navigate("add_book")
                },
                onBookClick = { book -> 
                    if (book.status == BookStatus.READY_TO_READ) {
                        navController.navigate("reading/${book.id}")
                    }
                }
            )
        }

        composable("add_book") {
            AddBookScreen(
                onNavigateToScanning = { bookId ->
                    navController.navigate("scanning/$bookId") {
                        popUpTo("add_book") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = "scanning/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: return@composable
            ScanningScreen(
                pageCount = pageCount,
                onTakePhoto = { pageCount++ },
                onFinishScanning = { navController.popBackStack() },
                bookId = bookId
            )
        }
        
        composable(
            route = "reading/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: return@composable
            val book = books.find { it.id == bookId } ?: return@composable
            
            ReadingScreen(
                bookTitle = book.title,
                currentPage = book.currentPage,
                currentParagraph = 1,
                isPlaying = isPlaying,
                onPlayPauseClick = { isPlaying = !isPlaying },
                onPreviousPageClick = { /* TODO */ },
                onNextPageClick = { /* TODO */ },
                onPreviousParagraphClick = { /* TODO */ },
                onNextParagraphClick = { /* TODO */ },
                onSpeedClick = { /* TODO */ }
            )
        }
    }
} 