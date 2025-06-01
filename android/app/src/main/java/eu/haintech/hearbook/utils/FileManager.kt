package eu.haintech.hearbook.utils

import android.content.Context
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileManager @Inject constructor() {
    companion object {
        private const val BOOKS_DIR = "books"
        private const val PAGE_PREFIX = "PAGE_"
        private const val PAGE_EXTENSION = ".jpg"
    }

    fun getBookDirectory(context: Context, bookId: Long): File {
        val storageDir = context.getExternalFilesDir("$BOOKS_DIR/$bookId")
        storageDir?.mkdirs()
        return storageDir ?: throw IllegalStateException("Nie można utworzyć katalogu dla książki")
    }

    fun createPageFile(context: Context, bookId: Long, timestamp: String): File {
        val bookDir = getBookDirectory(context, bookId)
        return File(bookDir, "$PAGE_PREFIX$timestamp$PAGE_EXTENSION")
    }

    fun getBookPages(context: Context, bookId: Long): List<File> {
        val bookDir = getBookDirectory(context, bookId)
        return if (bookDir.exists() && bookDir.isDirectory) {
            bookDir.listFiles()
                ?.filter { it.extension.lowercase() in listOf("jpg", "jpeg", "png") }
                ?.sortedBy { it.nameWithoutExtension }
                ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun getBookPath(context: Context, bookId: Long): String {
        return getBookDirectory(context, bookId).absolutePath
    }
} 