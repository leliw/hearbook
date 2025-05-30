package eu.haintech.hearbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.haintech.hearbook.model.Book
import eu.haintech.hearbook.model.BookStatus
import eu.haintech.hearbook.ui.screens.AddBookScreen
import eu.haintech.hearbook.ui.screens.BookListScreen
import eu.haintech.hearbook.ui.screens.ReadingScreen
import eu.haintech.hearbook.ui.screens.ScanningScreen
import eu.haintech.hearbook.ui.theme.HearBookTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HearBookTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HearBookApp()
                }
            }
        }
    }
}

@Composable
fun HearBookApp() {
    val navController = rememberNavController()
    
    // Temporary state for demonstration
    var pageCount by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    
    val sampleBooks = remember {
        listOf(
            Book(
                id = UUID.randomUUID().toString(),
                title = "Wojna i Pokój",
                status = BookStatus.READY_TO_READ,
                pageCount = 100,
                currentPage = 50,
                readingProgress = 0.5f
            ),
            Book(
                id = UUID.randomUUID().toString(),
                title = "Pan Tadeusz",
                status = BookStatus.SCANNING,
                pageCount = 20,
                currentPage = 0
            ),
            Book(
                id = UUID.randomUUID().toString(),
                title = "Lalka",
                status = BookStatus.PROCESSING
            )
        )
    }

    NavHost(navController = navController, startDestination = "books") {
        composable("books") {
            BookListScreen(
                books = sampleBooks,
                onAddBookClick = { navController.navigate("scanning") },
                onBookClick = { book -> 
                    if (book.status == BookStatus.READY_TO_READ) {
                        navController.navigate("reading/${book.id}")
                    }
                }
            )
        }
        
        composable("scanning") {
            ScanningScreen(
                pageCount = pageCount,
                onTakePhoto = { pageCount++ },
                onFinishScanning = { navController.popBackStack() }
            )
        }
        
        composable(
            route = "reading/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            val book = sampleBooks.find { it.id == bookId }
            
            book?.let {
                ReadingScreen(
                    bookTitle = it.title,
                    currentPage = it.currentPage,
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
} 