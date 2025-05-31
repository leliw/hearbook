package eu.haintech.hearbook.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val author: String? = null,
    val status: BookStatus = BookStatus.SCANNING,
    val pageCount: Int = 0,
    val currentPage: Int = 0,
    val readingProgress: Float = 0f,
    val createdAt: Long = System.currentTimeMillis()
) 