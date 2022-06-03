package com.example.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.data.model.Source
import java.time.LocalDateTime

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String,
    val timestamp: LocalDateTime
)
